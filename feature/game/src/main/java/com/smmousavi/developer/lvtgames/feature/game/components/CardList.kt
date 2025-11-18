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
import androidx.compose.ui.unit.dp
import com.smmousavi.developer.lvtgames.feature.cards.components.Card
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.CardUiModel
import com.smmousavi.developer.lvtgames.core.designsystem.R
import com.smmousavi.developer.lvtgames.core.designsystem.components.edgefade.EdgeFadeContainer
import com.smmousavi.developer.lvtgames.core.designsystem.components.edgefade.EdgeFadeSpec
import com.smmousavi.developer.lvtgames.core.designsystem.components.edgefade.ProvideEdgeFadeSpec
import com.smmousavi.developer.lvtgames.core.designsystem.components.edgefade.rememberEdgeFadeState
import com.smmousavi.developer.lvtgames.feature.cards.components.CellStyle
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.CardsUiModel
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.CellUiModel

@Composable
fun CardList(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    listState: LazyListState = rememberLazyListState(),
    cardsModel: CardsUiModel,
    isPreview: Boolean = false,
    onCardClick: ((CardUiModel, Int) -> Unit),
    onCellClicked: (cardId: Int, cellModel: CellUiModel, style: CellStyle) -> Unit,
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
                contentPadding = PaddingValues(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
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
    val mockCards = CardsUiModel(
        cards = listOf(
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