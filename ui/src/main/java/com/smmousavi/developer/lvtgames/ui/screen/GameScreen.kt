package com.smmousavi.developer.lvtgames.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smmousavi.developer.lvtgames.core.designsystem.components.EdgeFadeContainer
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.CardUiModel
import com.smmousavi.developer.lvtgames.feature.game.components.GameCardList

@Composable
fun GameScreen(
    cards: List<CardUiModel>,
    modifier: Modifier = Modifier,
    onCardClick: (CardUiModel) -> Unit = {},
) {
    EdgeFadeContainer(
        modifier = modifier,
        fadeRange = 64.dp,
        fadeRatio = 1f,
        fadeVertical = true,
        fadeHorizontal = false
    ) {
        GameCardList(
            cards = cards,
            modifier = Modifier.fillMaxSize(),
            onCardClick = onCardClick
        )
    }
}