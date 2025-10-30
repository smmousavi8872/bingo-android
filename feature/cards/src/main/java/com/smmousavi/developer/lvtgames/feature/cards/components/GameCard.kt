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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.smmousavi.developer.lvtgames.core.designsystem.components.BookmarkBadge
import com.smmousavi.developer.lvtgames.core.designsystem.components.StylousText
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.CardUiModel
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.PieceUiModel
import kotlin.math.floor
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
 * A single Bingo Card used inside the **GameBoard** and **GameCardList**.
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
    val cardBrush = rememberHorizontalBrush(
        start = cardModel.colors.startGradient,
        mid = cardModel.colors.midGradient,
        end = cardModel.colors.endGradient,
        softness = 0.25f
    )

    // Outer rounded layer
    BoxWithConstraints(
        modifier = modifier
            .padding(4.dp)
            .background(
                brush = cardBrush,
                shape = RoundedCornerShape(8.dp) // will keep, border scales below
            )
            .border(
                width = 1.5.dp, // outer frame stays thin to keep the style
                color = cardModel.colors.borderColor,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        // compute responsive scale
        val rows = cardModel.board.size
        val cols = cardModel.board.firstOrNull()?.size ?: 0
        val scale = rememberCardScaleState(
            rows = rows,
            cols = cols,
            maxW = maxWidth,
            maxH = maxHeight,
        )
        val cellSize: Dp = scale.cell

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(scale.innerPad / 2)
                .clip(shape = RoundedCornerShape(8.dp))
                .border(
                    width = 1.5.dp,
                    color = cardModel.colors.background.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            // Rebuild the header row here with scaled fonts/borders
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(scale.innerPad)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    StylousText(
                        modifier = Modifier.padding(
                            end = scale.innerPad * 0.5f
                        ),
                        text = cardModel.name,
                        color = cardModel.colors.titleColor,
                        fontSize = scale.titleSp,
                        fontWeight = FontWeight.Bold,
                        strokeWidth = (scale.border).coerceIn(1.dp, 1.5.dp),
                        strokeColor = Color(0xFF746343),
                        shadowColor = Color(0xFF000000).copy(alpha = 0.5f),
                        shadowOffsetX = (scale.border * 1.2f),
                        shadowOffsetY = (scale.border * 1.2f),
                        shadowBlur = scale.border,
                        letterSpacingEm = 0.02f
                    )
                    VerticalDivider(
                        color = cardModel.colors.background,
                        modifier = Modifier
                            .height((cellSize * 0.5f).coerceIn(14.dp, 22.dp))
                            .width(scale.border)
                    )
                    VerticalDivider(
                        color = cardModel.colors.borderColor,
                        modifier = Modifier
                            .height((cellSize * 0.5f).coerceIn(14.dp, 22.dp))
                            .width(scale.border)
                    )
                    StylousText(
                        modifier = Modifier.padding(start = scale.innerPad * 0.5f),
                        text = "Card Bet:",
                        color = cardModel.colors.titleColor,
                        fontSize = scale.labelSp,
                        fontWeight = FontWeight.Bold,
                        strokeWidth = (scale.border).coerceIn(1.dp, 1.5.dp),
                        strokeColor = Color(0xFF746343),
                        shadowColor = Color(0xFF000000).copy(alpha = 0.5f),
                        shadowOffsetX = (scale.border * 1.2f),
                        shadowOffsetY = (scale.border * 1.2f),
                        shadowBlur = scale.border,
                        letterSpacingEm = 0.02f
                    )
                    StylousText(
                        text = cardModel.bet.toString(),
                        color = cardModel.colors.titleColor,
                        fontSize = scale.valueSp,
                        fontWeight = FontWeight.Bold,
                        strokeWidth = (scale.border).coerceIn(1.dp, 1.5.dp),
                        strokeColor = Color(0xFF746343),
                        shadowColor = Color(0xFF000000).copy(alpha = 0.7f),
                        shadowOffsetX = (scale.border * 1.2f),
                        shadowOffsetY = (scale.border * 1.2f),
                        shadowBlur = scale.border,
                        letterSpacingEm = 0.02f
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    BookmarkBadge(
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(
                                top = (scale.innerPad * 0.25f),
                                end = (scale.innerPad),
                                bottom = (scale.innerPad * 0.25f),
                            ),
                        size = (cellSize * 0.7f).coerceIn(20.dp, 32.dp)
                    )
                }

                BoxWithConstraints(
                    modifier = Modifier
                        .wrapContentWidth()
                        .border(
                            width = scale.border,
                            color = cardModel.colors.borderColor,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .clip(RoundedCornerShape(4.dp))
                ) {
                    val colsCount = max(cols, 1)
                    val density = LocalDensity.current
                    // Compute a safe cellSize using floor(px)
                    val cellSize: Dp = with(density) {
                        val maxWpx = maxWidth.toPx()
                        val cellPx = floor(maxWpx / colsCount)
                        cellPx.toDp()
                    }
                    // Constrain the grid width EXACTLY to cols * cellSize
                    val gridWidth: Dp = cellSize * colsCount

                    Column(
                        modifier = Modifier.width(gridWidth),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        repeat(rows) { i ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
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

