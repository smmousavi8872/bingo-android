package com.smmousavi.developer.lvtgames.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.smmousavi.developer.lvtgames.core.designsystem.R
import com.smmousavi.developer.lvtgames.core.designsystem.components.ring.RingButton
import com.smmousavi.developer.lvtgames.core.designsystem.components.ring.RingSpec
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.CardUiModel
import com.smmousavi.developer.lvtgames.feature.game.components.GameBoard

@Composable
fun GameScreen(
    cards: List<CardUiModel>,
//    selectedCard: CardUiModel,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Game Board Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
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