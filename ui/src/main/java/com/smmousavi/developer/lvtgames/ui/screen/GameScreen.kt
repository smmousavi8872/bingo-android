package com.smmousavi.developer.lvtgames.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.CardUiModel
import com.smmousavi.developer.lvtgames.feature.game.components.GameCardList

@Composable
fun GameScreen(
    cards: List<CardUiModel>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onCardClick: (CardUiModel) -> Unit = {},
) {
    GameCardList(
        cards = cards,
        modifier = modifier,
        contentPadding = contentPadding,
        onCardClick = onCardClick
    )
}