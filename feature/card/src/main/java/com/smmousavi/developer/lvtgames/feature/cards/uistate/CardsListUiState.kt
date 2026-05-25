package com.smmousavi.developer.lvtgames.feature.cards.uistate

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import kotlin.Int

@Immutable
data class CardsListUiState(
    val cards: List<CardUiState>,
)

@Immutable
data class CardUiState(
    val id: Int,
    val name: String,
    val board: List<List<CellUiState>>,
    val bet: Int,
    val colors: CardColorsUiState,
) {
    companion object {
        val DEFAULT = CardUiState(
            id = 1,
            name = "Card 1",
            board = defaultBoard(),
            bet = 2100,
            colors = CardColorsUiState.DEFAULT
        )
    }
}

@Immutable
data class PrizeUiState(
    val id: Int,
    val cardId: Int,
    val title: String,
    val amount: Int,
    val type: String?,
    val number: Int?,
) {
    companion object {
        val DEFAULT = PrizeUiState(
            id = 1,
            cardId = 1,
            title = "First Five",
            amount = 100,
            type = "cash",
            number = 75
        )
    }
}

@Immutable
data class CellUiState(
    val position: Pair<Int, Int>,
    val value: Int,
    val prize: PrizeUiState?,
    val colors: CellColorsUiState,
) {
    companion object {
        val DEFAULT_VALUE = CellUiState(
            position = 0 to 0,
            value = 73,
            prize = null,
            colors = CellColorsUiState.DEFAULT
        )
        val DEFAULT_EMPTY = CellUiState(
            position = 0 to 0,
            value = -1,
            prize = null,
            colors = CellColorsUiState.DEFAULT
        )
        val DEFAULT_PRIZE = CellUiState(
            position = 0 to 0,
            value = 32,
            prize = PrizeUiState.DEFAULT,
            colors = CellColorsUiState.DEFAULT
        )
    }
}

@Immutable
data class CardColorsUiState(
    val background: Color,
    val startGradient: Color,
    val midGradient: Color,
    val endGradient: Color,
    val titleColor: Color,
    val borderColor: Color,
) {
    companion object {
        val DEFAULT = CardColorsUiState(
            background = Color(0xff54c62f),
            startGradient = Color(0xff2f8201),
            midGradient = Color(0xff54c62f),
            endGradient = Color(0xff2e7f02),
            titleColor = Color(0xfff8c317),
            borderColor = Color(0xff3d7b31),
        )
    }
}

@Immutable
data class CellColorsUiState(
    val background: Color,
    val textOnValue: Color,
    val textOnPrize: Color,
    val prizeOuterRing: Color,
    val prizeInnerRing: Color,
    val prizeInnerFill: Color,
    val highlightOverlay: Color? = null,
    val selectedOverlay: Color? = null,
) {
    companion object {
        val DEFAULT = CellColorsUiState(
            background = Color(0xff54c62f),
            textOnValue = Color(0xff3d7b31),
            textOnPrize = Color(0xFF428C35),
            prizeOuterRing = Color(0xff3d7b31),
            prizeInnerRing = Color(0xff54c62f),
            prizeInnerFill = Color(0xffecfed6),
            highlightOverlay = null,
            selectedOverlay = null,
        )
    }
}

private fun defaultBoard(): List<List<CellUiState>> = listOf(
    listOf(
        CellUiState.DEFAULT_PRIZE,
        CellUiState.DEFAULT_EMPTY,
        CellUiState.DEFAULT_PRIZE,
        CellUiState.DEFAULT_EMPTY,
        CellUiState.DEFAULT_PRIZE,
        CellUiState.DEFAULT_EMPTY,
        CellUiState.DEFAULT_PRIZE,
        CellUiState.DEFAULT_PRIZE,
        CellUiState.DEFAULT_EMPTY,
    ),
    listOf(
        CellUiState.DEFAULT_EMPTY,
        CellUiState.DEFAULT_VALUE,
        CellUiState.DEFAULT_EMPTY,
        CellUiState.DEFAULT_PRIZE,
        CellUiState.DEFAULT_EMPTY,
        CellUiState.DEFAULT_VALUE,
        CellUiState.DEFAULT_EMPTY,
        CellUiState.DEFAULT_VALUE,
        CellUiState.DEFAULT_PRIZE,
    ),
    listOf(
        CellUiState.DEFAULT_PRIZE,
        CellUiState.DEFAULT_EMPTY,
        CellUiState.DEFAULT_EMPTY,
        CellUiState.DEFAULT_VALUE,
        CellUiState.DEFAULT_PRIZE,
        CellUiState.DEFAULT_EMPTY,
        CellUiState.DEFAULT_VALUE,
        CellUiState.DEFAULT_VALUE,
        CellUiState.DEFAULT_EMPTY,
    )
)