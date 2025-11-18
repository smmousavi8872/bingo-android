package com.smmousavi.developer.lvtgames.feature.game.components

import android.annotation.SuppressLint
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

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CardList(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    listState: LazyListState = rememberLazyListState(),
    cards: List<CardUiModel>,
    onCardClick: (CardUiModel, Int) -> Unit,
) {
    val edgeFadeState = rememberEdgeFadeState(listState)

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
                .wrapContentWidth()
                .fillMaxHeight(),
            contentPadding = contentPadding,
            topFadeStrength = edgeFadeState.top.value,
            bottomFadeStrength = edgeFadeState.bottom.value
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.wrapContentSize(),
                contentPadding = PaddingValues(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                itemsIndexed(
                    items = cards,
                ) { index, model ->
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        // scale logo by card width
                        val logoSize = maxWidth * 0.20f // 20% of card width
                        val logoOverlap = logoSize * 0.24f // overlap upward by 26% of logo size
                        Card(
                            cardModel = model,
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center),
                            onClickCard = { onCardClick(model, index) },
                            onClickCell = { }
                        )
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
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCardList() {
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

    CardList(
        cards = mockCards,
        listState = rememberLazyListState(),
        onCardClick = { selected, index ->
            println("Clicked card: ${selected.name}, index = $index")
        }
    )
}