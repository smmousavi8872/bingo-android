package com.smmousavi.developer.lvtgames.feature.cards.components


import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.PieceUiModel

/**
 * Piece of the bongo board Composable component.
 * Draw order:
 * - token inner fill (behind)
 * - drawContent() -> Cell value
 * - token rings (over)
 * - optional overlay (over)
 *
 * @param pieceModel The state model of Piece.
 * @param style Style of cell that can be Prize, Value, or Empty.
 * @param state Visual state that cna be Normal, Disabled, Selected, Highlighted.
 * @param cellSize Width abd height of the square piece, default = 64dp.
 * @param onClickPiece Optional click handler (adds ripple if provided).

 */
@Composable
fun GamePiece(
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
        targetValue = if (state == PieceState.Disabled) baseBackground.copy(alpha = 0.5f) else baseBackground,
        label = "piece-bg"
    )
    val clickable = if (onClickPiece != null)
        Modifier.clickable(
            enabled = state != PieceState.Disabled,
            onClick = { onClickPiece(pieceModel) })
    else Modifier

    Surface(
        modifier = modifier
            .size(cellSize)
            .then(clickable),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = backgroundAnim,
                )
                .border(width = 0.5.dp, color = pieceModel.colors.prizeOuterRing)
                .drawWithContent {
                    val w = size.width
                    val h = size.height
                    val d = minOf(w, h)
                    val cx = w / 2f
                    val cy = h / 2f
                    val outerR = d * 0.38f
                    val midR = d * 0.30f


                    // Draw prize background (inner fill)
                    if (style == PieceStyle.Prize) {
                        drawCircle(
                            color = pieceModel.colors.prizeInnerFill,
                            radius = outerR,
                            center = Offset(cx, cy)
                        )
                    }
                    // Draw cell content if it is not empty
                    if (style != PieceStyle.Empty) drawContent()

                    // Draw inner an outer rings
                    if (style == PieceStyle.Prize) {
                        drawCircle(
                            color = pieceModel.colors.prizeOuterRing.let {
                                if (state == PieceState.Disabled) it.copy(alpha = 0.5f) else it
                            },
                            radius = outerR,
                            center = Offset(cx, cy),
                            style = Stroke(width = d * 0.06f, cap = StrokeCap.Round)
                        )
                        drawCircle(
                            color = pieceModel.colors.prizeInnerRing.let {
                                if (state == PieceState.Disabled) it.copy(alpha = 0.5f) else it
                            },
                            radius = midR,
                            center = Offset(cx, cy),
                            style = Stroke(width = d * 0.05f, cap = StrokeCap.Round)
                        )
                    }

                    // Draw overlay on top
                    when (state) {
                        PieceState.Highlighted -> pieceModel.colors.highlightOverlay
                        PieceState.Selected -> pieceModel.colors.selectedOverlay
                        else -> null
                    }?.let {
                        drawRect(color = it, size = size)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            // Cell value
            if (pieceModel.value >= 0) {
                val textColor = when (style) {
                    PieceStyle.Prize -> pieceModel.colors.textOnPrize
                    PieceStyle.Value -> pieceModel.colors.textOnValue
                    PieceStyle.Empty -> Color.Transparent
                }
                Text(
                    text = pieceModel.value.toString(),
                    color = if (state == PieceState.Disabled) {
                        textColor.copy(alpha = 0.5f)
                    } else {
                        textColor
                    },
                    fontSize = if (style == PieceStyle.Prize) {
                        14.sp
                    } else {
                        20.sp
                    },
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ValuePiecePreview() {
    GamePiece(
        pieceModel = PieceUiModel.DEFAULT_VALUE,
        style = PieceStyle.Value,
        onClickPiece = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun PrizePiecePreview() {
    GamePiece(
        pieceModel = PieceUiModel.DEFAULT_PRIZE,
        style = PieceStyle.Prize,
        onClickPiece = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun EmptyPiecePreview() {
    GamePiece(
        pieceModel = PieceUiModel.DEFAULT_EMPTY,
        style = PieceStyle.Empty,
        onClickPiece = {}
    )
}

