package com.smmousavi.developer.lvtgames.feature.cards.components


import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
enum class CellStyle { Empty, Prize, Value }

/**
 * State of a [Cell] for coloring/decoration logic.
 */
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

    val baseBackground = when (style) {
        CellStyle.Empty -> cellModel.colors.background
        CellStyle.Prize -> Color.White
        CellStyle.Value -> Color.White
    }

    val backgroundAnim by animateColorAsState(
        targetValue = if (state == CellState.Disabled)
            baseBackground.copy(alpha = 0.5f)
        else baseBackground,
        label = "cell-bg"
    )

    val strokeOuter = (cellSize * 0.08f).coerceAtLeast(1.dp)
    val strokeInner = (cellSize * 0.06f).coerceAtLeast(0.8.dp)
    val borderWidth = (cellSize * 0.01f).coerceAtLeast(0.5.dp)

    val fillColor = if (style == CellStyle.Prize) cellModel.colors.prizeInnerFill else null

    val outerRingSpec = if (style == CellStyle.Prize)
        RingSpec(
            color = cellModel.colors.prizeOuterRing.let { c ->
                if (state == CellState.Disabled) c.copy(alpha = 0.5f) else c
            },
            width = strokeOuter,
            radiusRatio = 0.38f
        ) else null

    val innerRingSpec = if (style == CellStyle.Prize)
        RingSpec(
            color = cellModel.colors.prizeInnerRing.let { c ->
                if (state == CellState.Disabled) c.copy(alpha = 0.5f) else c
            },
            width = strokeInner,
            radiusRatio = 0.30f
        ) else null

    Surface(
        modifier = modifier
            .size(cellSize)
            .then(
                if (onClickCell != null) {
                    Modifier.clickable(
                        enabled = state != CellState.Disabled,
                        onClick = { onClickCell(cellModel) }
                    )
                } else {
                    Modifier
                }
            ),
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
            onClick = onClickCell?.let { { it(cellModel) } }
        ) {
            if (cellModel.value >= 0) {
                val textColor = when (style) {
                    CellStyle.Prize -> cellModel.colors.textOnPrize
                    CellStyle.Value -> cellModel.colors.textOnValue
                    CellStyle.Empty -> Color.Transparent
                }.let { if (state == CellState.Disabled) it.copy(alpha = 0.5f) else it }

                Text(
                    text = cellModel.value.toString(),
                    color = textColor,
                    fontSize = if (style == CellStyle.Prize) (cellSize.value * 0.35f).sp
                    else (cellSize.value * 0.5f).sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
            }

            when (state) {
                CellState.Selected -> {

                }

                else -> {}
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

