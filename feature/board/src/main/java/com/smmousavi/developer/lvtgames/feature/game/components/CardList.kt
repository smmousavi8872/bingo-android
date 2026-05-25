package com.smmousavi.developer.lvtgames.feature.game.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.smmousavi.developer.lvtgames.feature.cards.components.Card
import com.smmousavi.developer.lvtgames.feature.cards.uistate.CardUiState
import com.smmousavi.developer.lvtgames.core.designsystem.R
import com.smmousavi.developer.lvtgames.core.designsystem.components.edgefade.EdgeFadeContainer
import com.smmousavi.developer.lvtgames.core.designsystem.components.edgefade.EdgeFadeSpec
import com.smmousavi.developer.lvtgames.core.designsystem.components.edgefade.ProvideEdgeFadeSpec
import com.smmousavi.developer.lvtgames.core.designsystem.components.edgefade.rememberEdgeFadeState
import com.smmousavi.developer.lvtgames.feature.cards.components.CellStyle
import com.smmousavi.developer.lvtgames.feature.cards.uistate.CardsListUiState
import com.smmousavi.developer.lvtgames.feature.cards.uistate.CellUiState

@Composable
fun CardList(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(12.dp),
    itemSpace: Dp = 8.dp,
    listState: LazyListState = rememberLazyListState(),
    cardsModel: CardsListUiState,
    isPreview: Boolean = false,
    onCardClick: ((CardUiState, Int) -> Unit),
    onCellClicked: (cardId: Int, cellModel: CellUiState, style: CellStyle) -> Unit,
) {
    val edgeFadeState = rememberEdgeFadeState(listState)
    val logoPainter = painterResource(id = R.drawable.logo)

    ProvideEdgeFadeSpec(
        EdgeFadeSpec(
            fadeRange = 48.dp,
            fadeRatio = 1f,
            vertical = true,
            horizontal = false,
        )
    ) {
        EdgeFadeContainer(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentPadding = contentPadding,
            topFadeStrength = edgeFadeState.top.value,
            bottomFadeStrength = edgeFadeState.bottom.value
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = contentPadding,
                verticalArrangement = Arrangement.spacedBy(itemSpace),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                itemsIndexed(
                    items = cardsModel.cards,
                    key = { _, card -> card.id }
                ) { index, card ->
                    BoxWithConstraints(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // scale logo by card width
                        val logoSize = maxWidth * 0.20f // 20% of card width
                        val logoOverlap = logoSize * 0.36f // overlap upward by ~36% of logo size

                        Card(
                            cardModel = card,
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center),
                            isPreview = isPreview,
                            onCardClicked = { onCardClick(card, index) },
                            onCellClicked = { cardId, cellModel, style ->
                                onCellClicked(cardId, cellModel, style)
                            }
                        )

                        // logo overlay on top center
                        Image(
                            painter = logoPainter,
                            contentDescription = "Game Logo",
                            modifier = Modifier
                                .size(logoSize)
                                .align(Alignment.TopCenter)
                                .offset(y = -logoOverlap),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCardList() {
    val mockCards = CardsListUiState(
        cards = listOf(
            CardUiState.DEFAULT,
            CardUiState.DEFAULT.copy(
                id = 2,
                name = "Card 2",
                colors = CardUiState.DEFAULT.colors.copy(
                    background = Color(0xFFC345A6),
                    startGradient = Color(0xFFE939DE),
                    midGradient = Color(0xFF076D18),
                    endGradient = Color(0xFF652146)
                )
            ),
            CardUiState.DEFAULT.copy(
                id = 3,
                name = "Card 3",
                colors = CardUiState.DEFAULT.colors.copy(
                    background = Color(0xFF579294),
                    startGradient = Color(0xFF4ACCB0),
                    midGradient = Color(0xFF46279A),
                    endGradient = Color(0xFF717B03)
                )
            )
        )
    )

    CardList(
        cardsModel = mockCards,
        listState = rememberLazyListState(),
        onCardClick = { selected, index ->
            println("Clicked card: ${selected.name}, index = $index")
        },
        onCellClicked = { _, _, _ -> },
    )
}