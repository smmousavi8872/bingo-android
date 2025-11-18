package com.smmousavi.developer.lvtgames.feature.cards.uimodel

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import kotlin.Int

@Immutable
data class CardsListUiModel(
    val cards: List<CardUiModel>,
)

@Immutable
data class CardUiModel(
    val id: Int,
    val name: String,
    val board: List<List<CellUiModel>>,
    val bet: Int,
    val colors: CardUiColors,
) {
    companion object {
        val DEFAULT = CardUiModel(
            id = 1,
            name = "Card 1",
            board = defaultBoard(),
            bet = 2100,
            colors = CardUiColors.DEFAULT
        )
    }
}

@Immutable
data class PrizeUiModel(
    val id: Int,
    val cardId: Int,
    val title: String,
    val amount: Int,
    val type: String?,
    val number: Int?,
) {
    companion object {
        val DEFAULT = PrizeUiModel(
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
data class CellUiModel(
    val position: Pair<Int, Int>,
    val value: Int,
    val prize: PrizeUiModel?,
    val colors: CellUiColors,
) {
    companion object {
        val DEFAULT_VALUE = CellUiModel(
            position = 0 to 0,
            value = 73,
            prize = null,
            colors = CellUiColors.DEFAULT
        )
        val DEFAULT_EMPTY = CellUiModel(
            position = 0 to 0,
            value = -1,
            prize = null,
            colors = CellUiColors.DEFAULT
        )
        val DEFAULT_PRIZE = CellUiModel(
            position = 0 to 0,
            value = 32,
            prize = PrizeUiModel.DEFAULT,
            colors = CellUiColors.DEFAULT
        )
    }
}

@Immutable
data class CardUiColors(
    val background: Color,
    val startGradient: Color,
    val midGradient: Color,
    val endGradient: Color,
    val titleColor: Color,
    val borderColor: Color,
) {
    companion object {
        val DEFAULT = CardUiColors(
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
data class CellUiColors(
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
        val DEFAULT = CellUiColors(
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

private fun defaultBoard(): List<List<CellUiModel>> = listOf(
    listOf(
        CellUiModel.DEFAULT_PRIZE,
        CellUiModel.DEFAULT_EMPTY,
        CellUiModel.DEFAULT_PRIZE,
        CellUiModel.DEFAULT_EMPTY,
        CellUiModel.DEFAULT_PRIZE,
        CellUiModel.DEFAULT_EMPTY,
        CellUiModel.DEFAULT_PRIZE,
        CellUiModel.DEFAULT_PRIZE,
        CellUiModel.DEFAULT_EMPTY,
    ),
    listOf(
        CellUiModel.DEFAULT_EMPTY,
        CellUiModel.DEFAULT_VALUE,
        CellUiModel.DEFAULT_EMPTY,
        CellUiModel.DEFAULT_PRIZE,
        CellUiModel.DEFAULT_EMPTY,
        CellUiModel.DEFAULT_VALUE,
        CellUiModel.DEFAULT_EMPTY,
        CellUiModel.DEFAULT_VALUE,
        CellUiModel.DEFAULT_PRIZE,
    ),
    listOf(
        CellUiModel.DEFAULT_PRIZE,
        CellUiModel.DEFAULT_EMPTY,
        CellUiModel.DEFAULT_EMPTY,
        CellUiModel.DEFAULT_VALUE,
        CellUiModel.DEFAULT_PRIZE,
        CellUiModel.DEFAULT_EMPTY,
        CellUiModel.DEFAULT_VALUE,
        CellUiModel.DEFAULT_VALUE,
        CellUiModel.DEFAULT_EMPTY,
    )
)