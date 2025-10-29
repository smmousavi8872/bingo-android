package com.smmousavi.developer.lvtgames.feature.cards.components


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smmousavi.developer.lvtgames.core.designsystem.components.BookmarkBadge
import com.smmousavi.developer.lvtgames.core.designsystem.components.StylousText
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.CardUiModel
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.PieceUiModel
import kotlin.math.max

/**
 * Visual style for a piece.
 * - Square: a simple rounded square cell, used for the board grid.
 * - Token: a circular badge with concentric rings used to show marked numbers.
 */
enum class PieceStyle { Empty, Prize, Value }

/**
 * State of a piece for coloring/decoration logic.
 */
enum class PieceState { Normal, Highlighted, Selected, Disabled }

/**
 * Server-driven Bingo Card.
 *
 * - Outer rounded panel with gradient.
 * - Inner rounded panel (slightly inset) with the same gradient (softened by alpha).
 * - Top-centered game icon overlapping the top edge (negative Y offset).
 * - Grid is built from the card's matrix; each cell is a [CardPiece].
 */
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun GameCard(
    modifier: Modifier = Modifier,
    cardModel: CardUiModel,
    onClickPiece: (PieceUiModel) -> Unit,
) {
    val rows = cardModel.board.size
    val cols = cardModel.board.firstOrNull()?.size ?: 0
    val cardBrush = rememberHorizontalBrush(
        start = cardModel.colors.startGradient,
        mid = cardModel.colors.midGradient,
        end = cardModel.colors.endGradient
    )

    // Top-level container with space reserved for the overlapping icon
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(4.dp)
            .background(
                brush = cardBrush,
                shape = RoundedCornerShape(4.dp)
            )
            .border(
                width = 1.dp,
                color = cardModel.colors.borderColor,
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        // Outer rounded layer
        Box(
            modifier = Modifier
                .padding(4.dp)
                .border(
                    width = 1.5.dp,
                    color = cardModel.colors.background.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(4.dp)
                )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    StylousText(
                        modifier = Modifier.padding(start = 8.dp, end = 4.dp),
                        text = cardModel.name,
                        color = cardModel.colors.titleColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        strokeWidth = 1.dp,
                        strokeColor = Color(0xFF746343),
                        shadowColor = Color(0xFF000000).copy(alpha = 0.5f),
                        shadowOffsetX = 1.dp,
                        shadowOffsetY = 1.dp,
                        shadowBlur = 1.dp,
                        letterSpacingEm = 0.02f
                    )
                    VerticalDivider(
                        color = cardModel.colors.background,
                        modifier = Modifier
                            .height(20.dp)
                            .width(1.dp)
                    )
                    VerticalDivider(
                        color = cardModel.colors.borderColor,
                        modifier = Modifier
                            .height(16.dp)
                            .width(1.dp)
                    )
                    StylousText(
                        modifier = Modifier.padding(start = 4.dp),
                        text = "Card Bet:",
                        color = cardModel.colors.titleColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        strokeWidth = 1.dp,
                        strokeColor = Color(0xFF746343),
                        shadowColor = Color(0xFF000000).copy(alpha = 0.5f),
                        shadowOffsetX = 1.dp,
                        shadowOffsetY = 1.dp,
                        shadowBlur = 1.dp,
                        letterSpacingEm = 0.02f
                    )
                    StylousText(
                        text = cardModel.bet.toString(),
                        color = cardModel.colors.titleColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        strokeWidth = 1.dp,
                        strokeColor = Color(0xFF746343),
                        shadowColor = Color(0xFF000000).copy(alpha = 0.7f),
                        shadowOffsetX = 1.dp,
                        shadowOffsetY = 1.dp,
                        shadowBlur = 1.dp,
                        letterSpacingEm = 0.02f
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    BookmarkBadge(
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(top = 4.dp, end = 16.dp),
                        size = 24.dp
                    )
                }
                // Inner rounded layer (slightly inset)
                Box(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                        .border(
                            width = 1.25.dp,
                            color = cardModel.colors.borderColor,
                            shape = RoundedCornerShape(4.dp)
                        )
                ) {
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(8.dp))

                    ) {
                        // maxWidth comes from BoxWithConstraintsScope (this is what the lint wants)
                        val colsCount = max(cols, 1)
                        val cellSize: Dp = (maxWidth / colsCount).coerceAtLeast(0.dp)

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            repeat(rows) { i ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val row = cardModel.board[i]
                                    repeat(colsCount) { j ->
                                        val pieceUiModel = row[j]
                                        CardPiece(
                                            pieceModel = pieceUiModel,
                                            cellSize = cellSize,
                                            style = getPieceStyle(pieceUiModel),
                                            onClickPiece = { onClickPiece(pieceUiModel) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Horizontal gradient with soft edges:
 * - start, mid, end are your three colors
 * - softness ∈ [0f, 0.45f] controls how wide the feathering is on both sides
 *   (higher = softer/longer fade at edges)
 */
@Composable
fun rememberHorizontalBrush(
    start: Color,
    mid: Color,
    end: Color,
    softness: Float = 0.2f,
): Brush {
    val s = softness.coerceIn(0f, 0.45f)
    val startFeather = lerp(start, mid, 0.55f)
    val endFeather = lerp(end, mid, 0.55f)

    return Brush.linearGradient(
        0f to start,
        s to startFeather, // soften left edge
        0.5f to mid, // dominant middle
        1f - s to endFeather, // soften right edge
        1f to end,
        start = Offset(0f, 0f), // left
        end = Offset(1000f, 0f) // right (direction only)
    )
}

private fun getPieceStyle(pieceUiModel: PieceUiModel) = if (pieceUiModel.prize == null) {
    if (pieceUiModel.value >= 0) {
        PieceStyle.Value
    } else {
        PieceStyle.Empty
    }
} else {
    PieceStyle.Prize
}


@Composable
@Preview(showBackground = true)
fun CardPreviewSample() {
    GameCard(
        cardModel = CardUiModel.DEFAULT,
        onClickPiece = {}
    )
}

