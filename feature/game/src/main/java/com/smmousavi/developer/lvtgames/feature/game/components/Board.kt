package com.smmousavi.developer.lvtgames.feature.game.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smmousavi.developer.lvtgames.feature.cards.components.CellStyle
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.CardsUiModel
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.CellUiModel
import kotlinx.coroutines.launch

@Composable
fun Board(
    modifier: Modifier = Modifier,
    cardsModel: CardsUiModel,
    onCellClicked: (cardId: Int, cellModel: CellUiModel, style: CellStyle) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val mainListState = rememberLazyListState()

    Row(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.Bottom,
    ) {
        // Main cards list (interactive)
        CardList(
            modifier = Modifier.weight(0.7f),
            cardsModel = cardsModel,
            isPreview = false,
            listState = mainListState,
            onCardClick = { _, index ->
                coroutineScope.launch {
                    mainListState.animateScrollToItem(index, 0)
                }
            },
            onCellClicked = onCellClicked
        )

        Spacer(modifier = Modifier.size(16.dp))

        // Preview list (read-only)
        CardList(
            modifier = Modifier.weight(0.3f),
            cardsModel = cardsModel,
            isPreview = true,
            onCardClick = { _, index ->
                coroutineScope.launch {
                    mainListState.animateScrollToItem(index, 0)
                }
            },
            onCellClicked = onCellClicked
        )
    }
}