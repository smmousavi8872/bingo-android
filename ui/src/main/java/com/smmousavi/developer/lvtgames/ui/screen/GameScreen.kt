package com.smmousavi.developer.lvtgames.ui.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.CardUiModel
import com.smmousavi.developer.lvtgames.feature.game.components.GameBoard

@Composable
fun GameScreen(
    cards: List<CardUiModel>,
//    selectedCard: CardUiModel,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        GameBoard(
            modifier = modifier,
            cards = cards,
//        selectedCard = selectedCard,
        )


    }

}