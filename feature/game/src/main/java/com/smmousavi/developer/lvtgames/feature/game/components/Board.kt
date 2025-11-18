package com.smmousavi.developer.lvtgames.feature.game.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.CardUiModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun Board(
    modifier: Modifier = Modifier,
    cards: List<CardUiModel>,
//    selectedCard: CardUiModel,
) {
    var currentCard by remember { mutableStateOf(cards[0]) }
    val coroutineScope = rememberCoroutineScope()
    val cardListState = rememberLazyListState()

    Row(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.Bottom,
    ) {
        CardList(
            modifier = Modifier.weight(0.75f),
            cards = cards,
            listState = cardListState,
            onCardClick = { _, _ ->
            }
        )

        Spacer(modifier = modifier.size(16.dp))

        CardList(
            modifier = Modifier.weight(0.25f),
            cards = cards,
            onCardClick = { card, index ->
                currentCard = card
                coroutineScope.launch {
                    cardListState.scrollToItem(index, 0)
                }
            }
        )
    }
}