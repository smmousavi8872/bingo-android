package com.smmousavi.developer.lvtgames.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.CardUiModel
import com.smmousavi.developer.lvtgames.feature.game.components.GameCardList

@Composable
fun GameScreen(
    cards: List<CardUiModel>,
    modifier: Modifier = Modifier,
    onCardClick: (CardUiModel) -> Unit = {},
) {
    Box(modifier = Modifier.fillMaxSize()) {
        GameCardList(cards)
    }
}