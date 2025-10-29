package com.smmousavi.developer.lvtgames.feature.game.components

import android.R.attr.maxWidth
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.smmousavi.developer.lvtgames.core.designsystem.R
import com.smmousavi.developer.lvtgames.feature.cards.components.GameCard
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.CardUiModel

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun GameBoard(
    modifier: Modifier = Modifier,
    cards: List<CardUiModel>,
//    selectedCard: CardUiModel,
) {
    var currentCard by remember { mutableStateOf(cards[0]) }

    Row(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.Bottom,
    ) {
        BoxWithConstraints(modifier = Modifier.weight(0.7f)) {
            // scale logo by card width
            val logoSize = maxWidth * 0.15f // 15% of card width
            val logoOverlap = logoSize * 0.32f // overlap upward by 28% of logo size

            GameCard(cardModel = currentCard) {
                // onPieceClick
            }
            // logo overlay on top center
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Game Logo",
                modifier = Modifier
                    .size(logoSize)
                    .align(Alignment.TopCenter)
                    .offset(y = -logoOverlap),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = modifier.size(16.dp))

        GameCardList(
            modifier = Modifier.weight(0.3f),
            cards = cards,
            contentPadding = PaddingValues(vertical = 16.dp),
            onCardClick = { card ->
                currentCard = card
            }
        )
    }
}