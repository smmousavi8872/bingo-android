package com.smmousavi.developer.lvtgames.feature.cards.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smmousavi.developer.lvtgames.core.designsystem.components.ring.RingButton
import com.smmousavi.developer.lvtgames.core.designsystem.components.ring.RingSpec
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.CellUiModel

/**
 * Visual style for a [Cell].
 * - Square: a simple rounded square cell, used for the board grid.
 * - Token: a circular badge with concentric rings used to show marked numbers.
 */
@Immutable
enum class CellStyle { Empty, Prize, Value }

/**
 * State of a [Cell] for coloring/decoration logic.
 */
@Immutable
enum class CellState { Normal, Highlighted, Selected, Disabled }

/**
 * A single **board cell** displaying either a number, a prize token, or an empty slot.
 *
 * Renders a responsive square cell using [RingButton] with these visual layers:
 * 1. Background
 * 2. Optional filled circle and concentric rings (for [CellStyle.Prize])
 * 3. Cell content (number)
 * 4. Optional overlay (for highlight or selection)
 *
 * Behavior:
 * - Disabled cells appear dimmed.
 * - Highlighted or selected cells draw a semi-transparent overlay.
 * - If [onClickCell] is provided, the cell is clickable (unless disabled).
 *
 * All sizes, strokes, and text scale from [cellSize] for consistent proportions.
 *
 * @param modifier Modifier for layout and styling.
 * @param cellModel Cell data (number, prize, and color palette).
 * @param style Visual style: [CellStyle.Empty], [CellStyle.Value], or [CellStyle.Prize].
 * @param state Visual state: [CellState.Normal], [CellState.Highlighted], [CellState.Selected], or [CellState.Disabled].
 * @param cellSize Width and height of the cell; defines all internal scaling.
 * @param onClickCell Optional click callback that passes the current [cellModel].
 */
@Composable
fun Cell(
    modifier: Modifier = Modifier,
    cellModel: CellUiModel,
    style: CellStyle = CellStyle.Empty,
    state: CellState = CellState.Normal,
    cellSize: Dp = 64.dp,
    onClickCell: ((CellUiModel) -> Unit)?,
) {
    val isDisabled = state == CellState.Disabled

    // Base background depends only on style + model colors
    val baseBackground = remember(style, cellModel.colors.background) {
        when (style) {
            CellStyle.Empty -> cellModel.colors.background
            CellStyle.Prize -> Color.White
            CellStyle.Value -> Color.White
        }
    }

    // Animated background driven only by disabled state changes
    val targetBackground = remember(baseBackground, isDisabled) {
        if (isDisabled) baseBackground.copy(alpha = 0.5f) else baseBackground
    }
    val backgroundAnim by animateColorAsState(
        targetValue = targetBackground,
        label = "cell-bg"
    )

    // Precompute sizes from cellSize once
    val strokeOuter = remember(cellSize) { (cellSize * 0.08f).coerceAtLeast(1.dp) }
    val strokeInner = remember(cellSize) { (cellSize * 0.06f).coerceAtLeast(0.8.dp) }
    val borderWidth = remember(cellSize) { (cellSize * 0.01f).coerceAtLeast(0.5.dp) }

    // Prize fill color (only relevant for prize style)
    val fillColor = remember(style, cellModel.colors.prizeInnerFill) {
        if (style == CellStyle.Prize) cellModel.colors.prizeInnerFill else null
    }

    // Outer & inner ring specs: only created when style == Prize
    val outerRingSpec = remember(style, state, cellModel.colors.prizeOuterRing, strokeOuter) {
        if (style == CellStyle.Prize) {
            val c = cellModel.colors.prizeOuterRing
            RingSpec(
                color = if (isDisabled) c.copy(alpha = 0.5f) else c,
                width = strokeOuter,
                radiusRatio = 0.38f
            )
        } else null
    }

    val innerRingSpec = remember(style, state, cellModel.colors.prizeInnerRing, strokeInner) {
        if (style == CellStyle.Prize) {
            val c = cellModel.colors.prizeInnerRing
            RingSpec(
                color = if (isDisabled) c.copy(alpha = 0.5f) else c,
                width = strokeInner,
                radiusRatio = 0.30f
            )
        } else null
    }

    // Text color derived from style + state
    val textColor = remember(style, state, cellModel.colors) {
        val base = when (style) {
            CellStyle.Prize -> cellModel.colors.textOnPrize
            CellStyle.Value -> cellModel.colors.textOnValue
            CellStyle.Empty -> Color.Transparent
        }
        if (isDisabled) base.copy(alpha = 0.5f) else base
    }

    // Font size scales with the cell size and style
    val fontSize = remember(cellSize, style) {
        if (style == CellStyle.Prize) {
            (cellSize.value * 0.35f).sp
        } else {
            (cellSize.value * 0.5f).sp
        }
    }

    // Single, stable click lambda passed down, or null when disabled / no handler
    val ringClick: (() -> Unit)? = remember(onClickCell, cellModel, isDisabled) {
        if (onClickCell != null && !isDisabled) {
            { onClickCell(cellModel) }
        } else {
            null
        }
    }

    Surface(
        modifier = modifier.size(cellSize),
        color = Color.Transparent
    ) {
        RingButton(
            size = cellSize,
            backgroundColor = backgroundAnim,
            borderColor = cellModel.colors.prizeOuterRing,
            borderWidth = borderWidth,
            filledCircleColor = fillColor,
            outerRing = outerRingSpec,
            innerRing = innerRingSpec,
            onClick = ringClick
        ) {
            if (cellModel.value >= 0) {
                Text(
                    text = cellModel.value.toString(),
                    color = textColor,
                    fontSize = fontSize,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

fun getStyle(cellUiModel: CellUiModel) = if (cellUiModel.prize == null) {
    if (cellUiModel.value >= 0) {
        CellStyle.Value
    } else {
        CellStyle.Empty
    }
} else {
    CellStyle.Prize
}

@Preview(showBackground = true)
@Composable
private fun ValueCellPreview() {
    Cell(
        cellModel = CellUiModel.DEFAULT_VALUE,
        onClickCell = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun PrizeCellPreview() {
    Cell(
        cellModel = CellUiModel.DEFAULT_PRIZE,
        style = CellStyle.Prize,
        onClickCell = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun EmptyCellPreview() {
    Cell(
        cellModel = CellUiModel.DEFAULT_EMPTY,
        style = CellStyle.Empty,
        onClickCell = {}
    )
}