package com.smmousavi.developer.lvtgames.feature.cards.components


import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.PieceUiModel

/**
 * A single **board cell** displaying either a number, a prize token, or an empty slot.
 *
 * Renders a responsive square cell using [RingButton] with these visual layers:
 * 1. Background
 * 2. Optional filled circle and concentric rings (for [PieceStyle.Prize])
 * 3. Cell content (number)
 * 4. Optional overlay (for highlight or selection)
 *
 * Behavior:
 * - Disabled cells appear dimmed.
 * - Highlighted or selected cells draw a semi-transparent overlay.
 * - If [onClickPiece] is provided, the cell is clickable (unless disabled).
 *
 * All sizes, strokes, and text scale from [cellSize] for consistent proportions.
 *
 * @param modifier Modifier for layout and styling.
 * @param pieceModel Cell data (number, prize, and color palette).
 * @param style Visual style: [PieceStyle.Empty], [PieceStyle.Value], or [PieceStyle.Prize].
 * @param state Visual state: [PieceState.Normal], [PieceState.Highlighted],
 *              [PieceState.Selected], or [PieceState.Disabled].
 * @param cellSize Width and height of the cell; defines all internal scaling.
 * @param onClickPiece Optional click callback that passes the current [pieceModel].
 */
@Composable
fun CardPiece(
    modifier: Modifier = Modifier,
    pieceModel: PieceUiModel,
    style: PieceStyle = PieceStyle.Empty,
    state: PieceState = PieceState.Normal,
    cellSize: Dp = 64.dp,
    onClickPiece: ((PieceUiModel) -> Unit)?,
) {
    val baseBackground = when (style) {
        PieceStyle.Empty -> pieceModel.colors.background
        PieceStyle.Prize -> Color.White
        PieceStyle.Value -> Color.White
    }

    val backgroundAnim by animateColorAsState(
        targetValue = if (state == PieceState.Disabled)
            baseBackground.copy(alpha = 0.5f)
        else baseBackground,
        label = "piece-bg"
    )

    val strokeOuter = (cellSize * 0.08f).coerceAtLeast(1.dp)
    val strokeInner = (cellSize * 0.06f).coerceAtLeast(0.8.dp)
    val borderWidth = (cellSize * 0.01f).coerceAtLeast(0.5.dp)

    val fillColor = if (style == PieceStyle.Prize) pieceModel.colors.prizeInnerFill else null

    val outerRingSpec = if (style == PieceStyle.Prize)
        RingSpec(
            color = pieceModel.colors.prizeOuterRing.let { c ->
                if (state == PieceState.Disabled) c.copy(alpha = 0.5f) else c
            },
            width = strokeOuter,
            radiusRatio = 0.38f
        ) else null

    val innerRingSpec = if (style == PieceStyle.Prize)
        RingSpec(
            color = pieceModel.colors.prizeInnerRing.let { c ->
                if (state == PieceState.Disabled) c.copy(alpha = 0.5f) else c
            },
            width = strokeInner,
            radiusRatio = 0.30f
        ) else null

    Surface(
        modifier = modifier
            .size(cellSize)
            .then(
                if (onClickPiece != null) {
                    Modifier.clickable(
                        enabled = state != PieceState.Disabled,
                        onClick = { onClickPiece(pieceModel) }
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
            borderColor = pieceModel.colors.prizeOuterRing,
            borderWidth = borderWidth,
            filledCircleColor = fillColor,
            outerRing = outerRingSpec,
            innerRing = innerRingSpec,
            onClick = onClickPiece?.let { { it(pieceModel) } }
        ) {
            if (pieceModel.value >= 0) {
                val textColor = when (style) {
                    PieceStyle.Prize -> pieceModel.colors.textOnPrize
                    PieceStyle.Value -> pieceModel.colors.textOnValue
                    PieceStyle.Empty -> Color.Transparent
                }.let { if (state == PieceState.Disabled) it.copy(alpha = 0.5f) else it }

                Text(
                    text = pieceModel.value.toString(),
                    color = textColor,
                    fontSize = if (style == PieceStyle.Prize) (cellSize.value * 0.35f).sp
                    else (cellSize.value * 0.5f).sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
            }

            when (state) {
                PieceState.Highlighted -> pieceModel.colors.highlightOverlay
                PieceState.Selected -> pieceModel.colors.selectedOverlay
                else -> null
            }?.let {
                // simple overlay rectangle on top of content if desired
                Box(
                    Modifier
                        .matchParentSize()
                        .background(it)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ValuePiecePreview() {
    CardPiece(
        pieceModel = PieceUiModel.DEFAULT_VALUE,
        style = PieceStyle.Value,
        onClickPiece = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun PrizePiecePreview() {
    CardPiece(
        pieceModel = PieceUiModel.DEFAULT_PRIZE,
        style = PieceStyle.Prize,
        onClickPiece = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun EmptyPiecePreview() {
    CardPiece(
        pieceModel = PieceUiModel.DEFAULT_EMPTY,
        style = PieceStyle.Empty,
        onClickPiece = {}
    )
}

