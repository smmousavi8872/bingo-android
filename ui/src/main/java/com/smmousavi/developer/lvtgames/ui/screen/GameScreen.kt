package com.smmousavi.developer.lvtgames.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smmousavi.developer.lvtgames.core.designsystem.R
import com.smmousavi.developer.lvtgames.core.designsystem.components.StylousText
import com.smmousavi.developer.lvtgames.core.designsystem.components.ring.RingButton
import com.smmousavi.developer.lvtgames.core.designsystem.components.ring.RingSpec
import com.smmousavi.developer.lvtgames.feature.cards.CardsViewModel
import com.smmousavi.developer.lvtgames.feature.cards.components.CellStyle
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.CardsListUiModel
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.CellUiModel
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.newPrize
import com.smmousavi.developer.lvtgames.feature.game.components.Board
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.splash),
            contentDescription = "Game Board Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        StylousText(
            modifier = Modifier
                .padding(bottom = 24.dp)
                .align(Alignment.BottomCenter),
            text = "Loading Cards...",
            fontSize = 20.sp
        )
    }
}

@Composable
fun ErrorScreen(message: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        StylousText(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .align(Alignment.Center),
            text = message,
        )
    }
}
@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    cards: CardsListUiModel,
    viewModel: CardsViewModel = koinViewModel(),
) {
    val backgroundPainter = painterResource(id = R.drawable.background)
    val medalPainter = painterResource(id = R.drawable.ic_bingo_medal)
    val chatPainter = painterResource(id = R.drawable.ic_bingo_chat)

    val onCellClicked: (cardId: Int, cellModel: CellUiModel, style: CellStyle) -> Unit =
        remember(viewModel) {
            { cardId, cellModel, style ->
                when (style) {
                    CellStyle.Value -> {
                        viewModel.addCardPrize(
                            newPrize(
                                cardId = cardId,
                                number = cellModel.value
                            )
                        )
                    }

                    CellStyle.Prize -> {
                        cellModel.prize?.id?.let { prizeId ->
                            viewModel.deleteCardPrize(
                                cardId = cardId,
                                prizeId = prizeId,
                            )
                        }
                    }

                    CellStyle.Empty -> {
                        // No-op
                    }
                }
            }
        }

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
    ) {
        Image(
            painter = backgroundPainter,
            contentDescription = "Game Board Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        TopArch(
            width = maxWidth * 0.35f,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-12).dp)
        )

        Row(
            modifier = Modifier.padding(
                top = 16.dp,
                end = 24.dp,
                bottom = 32.dp,
                start = 24.dp
            ),
            verticalAlignment = Alignment.Bottom
        ) {
            Column(modifier = Modifier.padding(end = 8.dp)) {
                RingButton(
                    size = 56.dp,
                    filledCircleColor = Color(0xFFFFE082),
                    innerRing = RingSpec(
                        color = Color(0xFF7A2C23),
                        width = 2.5.dp,
                        radiusRatio = 0.32f
                    ),
                    outerRing = RingSpec(
                        color = Color(0xFFFFC107),
                        width = 4.dp,
                        radiusRatio = 0.39f
                    ),
                    onClick = {}
                ) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        painter = medalPainter,
                        contentDescription = "Score Icon",
                        tint = Color(0xFF7A2C23)
                    )
                }

                RingButton(
                    size = 56.dp,
                    filledCircleColor = Color(0xFFFFE082),
                    innerRing = RingSpec(
                        color = Color(0xFF7A2C23),
                        width = 2.5.dp,
                        radiusRatio = 0.32f
                    ),
                    outerRing = RingSpec(
                        color = Color(0xFFFFC107),
                        width = 4.dp,
                        radiusRatio = 0.39f
                    ),
                    onClick = {}
                ) {
                    Icon(
                        modifier = Modifier.size(42.dp),
                        painter = chatPainter,
                        contentDescription = "Chat Icon",
                        tint = Color(0xFF7A2C23)
                    )
                }
            }

            Board(
                cardsModel = cards,
                onCellClicked = onCellClicked
            )
        }
    }
}

/**
 * Decorative top arch header used above the board.
 */
@Composable
fun TopArch(
    modifier: Modifier = Modifier,
    width: Dp,
    height: Dp = 22.dp,
    colorStart: Color = Color(0xFFFFC043),
    colorEnd: Color = Color(0xFFE58A1F),
    borderColor: Color = Color(0xFFB96A12),
) {
    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .clip(
                RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = height,
                    bottomEnd = height
                )
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(colorStart, colorEnd)
                )
            )
            .border(
                width = 1.5.dp,
                color = borderColor,
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = height,
                    bottomEnd = height
                )
            )
    )
}