package com.smmousavi.developer.lvtgames.ui.screen

import android.annotation.SuppressLint
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.smmousavi.developer.lvtgames.core.designsystem.R
import com.smmousavi.developer.lvtgames.core.designsystem.components.ring.RingButton
import com.smmousavi.developer.lvtgames.core.designsystem.components.ring.RingSpec
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.CardUiModel
import com.smmousavi.developer.lvtgames.feature.game.components.GameBoard

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    cards: List<CardUiModel>,
//    selectedCard: CardUiModel,

) {
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.background),
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
                        painter = painterResource(R.drawable.ic_bingo_medal),
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
                        painter = painterResource(R.drawable.ic_bingo_chat),
                        contentDescription = "Chat Icon",
                        tint = Color(0xFF7A2C23)
                    )
                }
            }

            GameBoard(
                cards = cards,
//        selectedCard = selectedCard,
            )
        }
    }
}

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