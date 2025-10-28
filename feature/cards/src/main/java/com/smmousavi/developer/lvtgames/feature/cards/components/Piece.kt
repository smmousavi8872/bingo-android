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
import androidx.compose.ui.geometry.CornerRadius
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
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.PieceColors

/**
 * Visual style for a piece.
 * - Square: a simple rounded square cell, used for the board grid.
 * - Token: a circular badge with concentric rings used to show marked numbers.
 */
enum class PieceStyle { Square, Token }

/**
 * State of a piece for coloring/decoration logic.
 */
enum class PieceState { Normal, Highlighted, Selected, Disabled }

/**
 * Piece of the bongo board
 *
 * @param cellValue Optional number to show inside the piece.
 * @param colors The color values for the cell.
 * @param style Square grid cell or Token overlay with rings.
 * @param state Visual state (affects colors/alpha).
 * @param isSecondaryCell If true and style==Square, uses white background.
 * @param cellSize Width abd height of the square piece, default = 64dp.
 * @param onClick Optional click handler (adds ripple if provided).
 */
@Composable
fun Piece(
    modifier: Modifier = Modifier,
    cellValue: Int?,
    colors: PieceColors,
    style: PieceStyle = PieceStyle.Square,
    state: PieceState = PieceState.Normal,
    isSecondaryCell: Boolean = false,
    cellSize: Dp = 64.dp,
    onClick: (() -> Unit)? = null,
) {
    val baseBackground = when (style) {
        PieceStyle.Square -> if (isSecondaryCell) colors.secondaryCell else colors.primaryCell
        PieceStyle.Token -> Color.Transparent
    }

    val backgroundAnim by animateColorAsState(
        targetValue = if (state == PieceState.Disabled) baseBackground.copy(alpha = 0.5f) else baseBackground,
        label = "piece-bg"
    )

    val clickable = if (onClick != null)
        Modifier.clickable(enabled = state != PieceState.Disabled, onClick = onClick)
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
                    color = if (style == PieceStyle.Square) backgroundAnim else Color.Transparent,
                )
                .border(width = 2.dp, color = colors.tokenOuterRing)
                .drawWithContent {
                    val w = size.width
                    val h = size.height
                    val d = minOf(w, h)
                    val cx = w / 2f
                    val cy = h / 2f
                    val outerR = d * 0.38f
                    val midR = d * 0.30f

                    // Draw token background (inner fill)
                    if (style == PieceStyle.Token) {
                        drawCircle(
                            color = colors.tokenInnerFill,
                            radius = midR,
                            center = Offset(cx, cy)
                        )
                    }
                    // Draw cell content
                    drawContent()

                    // Draw inner an outer rings
                    if (style == PieceStyle.Token) {
                        drawCircle(
                            color = colors.tokenOuterRing.let {
                                if (state == PieceState.Disabled) it.copy(alpha = 0.5f) else it
                            },
                            radius = outerR,
                            center = Offset(cx, cy),
                            style = Stroke(width = d * 0.06f, cap = StrokeCap.Round)
                        )
                        drawCircle(
                            color = colors.tokenInnerRing.let {
                                if (state == PieceState.Disabled) it.copy(alpha = 0.5f) else it
                            },
                            radius = midR,
                            center = Offset(cx, cy),
                            style = Stroke(width = d * 0.05f, cap = StrokeCap.Round)
                        )
                    }

                    // Draw overlay on top
                    when (state) {
                        PieceState.Highlighted -> colors.highlightOverlay
                        PieceState.Selected -> colors.selectedOverlay
                        else -> null
                    }?.let {
                        drawRect(color = it, size = size)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            // Cell value
            cellValue?.let {
                val textColor = when {
                    style == PieceStyle.Token -> colors.textOnPrimary
                    isSecondaryCell -> colors.textOnSecondary
                    else -> colors.textOnPrimary
                }
                Text(
                    text = it.toString(),
                    color = if (state == PieceState.Disabled) textColor.copy(alpha = 0.5f) else textColor,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PieceSquareGreenPreview() {
    Piece(
        cellValue = null,
        style = PieceStyle.Square,
        colors = PieceColors.DEFAULT,
        isSecondaryCell = false
    )
}

@Preview(showBackground = true)
@Composable
private fun PieceSquareWhitePreview() {
    Piece(
        cellValue = 12,
        style = PieceStyle.Square,
        colors = PieceColors.DEFAULT,
        isSecondaryCell = true
    )
}

@Preview(showBackground = true)
@Composable
private fun PieceTokenPreview() {
    Piece(cellValue = 2, style = PieceStyle.Token, colors = PieceColors.DEFAULT)
}

