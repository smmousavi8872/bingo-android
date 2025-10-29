package com.smmousavi.developer.lvtgames.feature.game.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.smmousavi.developer.lvtgames.feature.cards.components.GameCard
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.CardUiModel

@Composable
fun GameCardList(
    cards: List<CardUiModel>,
    modifier: Modifier = Modifier,
    onCardClick: (CardUiModel) -> Unit = {},
) {
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
    ) {
        items(
            items = cards,
            key = { it.id }
        ) { model ->
            GameCard(
                cardModel = model,
                modifier = Modifier.fillMaxWidth(),
                onClickPiece = { onCardClick(model) }
            )
        }
    }
}

@Preview(
    name = "Game Card List Preview",
    showBackground = true,
    backgroundColor = 0xFF1B1B1B, // dark background to make colors pop
    widthDp = 412,
    heightDp = 732
)
@Composable
fun PreviewGameCardList() {
    // Create a few visually distinct copies of DEFAULT for variation
    val mockCards = listOf(
        CardUiModel.DEFAULT,
        CardUiModel.DEFAULT.copy(
            id = 2,
            name = "Card 2",
            colors = CardUiModel.DEFAULT.colors.copy(
                background = Color(0xFFC345A6),
                startGradient = Color(0xFFE939DE),
                midGradient = Color(0xFF076D18),
                endGradient = Color(0xFF652146)
            )
        ),
        CardUiModel.DEFAULT.copy(
            id = 3,
            name = "Card 3",
            colors = CardUiModel.DEFAULT.colors.copy(
                background = Color(0xFF579294),
                startGradient = Color(0xFF4ACCB0),
                midGradient = Color(0xFF46279A),
                endGradient = Color(0xFF717B03)
            )
        )
    )

    GameCardList(
        cards = mockCards,
        onCardClick = { selected ->
            println("Clicked card: ${selected.name}")
        }
    )
}


