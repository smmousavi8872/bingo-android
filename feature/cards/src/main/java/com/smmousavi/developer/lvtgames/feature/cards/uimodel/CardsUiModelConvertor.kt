package com.smmousavi.developer.lvtgames.feature.cards.uimodel

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.smmousavi.developer.lvtgames.core.model.domain.cards.CardsModel

fun CardsModel.toUiModel(): CardsUiModel =
    CardsUiModel(cards = cards.map { it.toUiModel() })

fun CardsModel.Card.toUiModel(): CardUiModel {
    return CardUiModel(
        id = id,
        name = name,
        board = matrix.toBoard(
            prizes = prizes.map { it.toPrizeUi() },
            colors = colors.toCellColors()
        ),
        bet = bet,
        colors = CardColors(
            background = colors.background.toColorOr(default = Color.White),
            startGradient = colors.backgroundGradient1.toColorOr(default = CardColors.DEFAULT.startGradient),
            midGradient = colors.backgroundGradient2.toColorOr(default = CardColors.DEFAULT.midGradient),
            endGradient = colors.backgroundGradient3.toColorOr(default = CardColors.DEFAULT.endGradient),
            titleColor = colors.titleColor.toColorOr(default = Color.White),
            borderColor = colors.borderColor.toColorOr(CardColors.DEFAULT.borderColor)
        )
    )
}

private fun CardsModel.CardColors.toCellColors(): CellColors {
    return CellColors(
        background = background.toColorOr(default = CellColors.DEFAULT.background),
        textOnValue = textColor.toColorOr(default = CellColors.DEFAULT.textOnValue),
        textOnPrize = textColor.toColorOr(default = CellColors.DEFAULT.textOnPrize),
        prizeOuterRing = borderColor.toColorOr(default = CellColors.DEFAULT.prizeOuterRing),
        prizeInnerRing = background.toColorOr(default = CellColors.DEFAULT.prizeInnerRing),
        prizeInnerFill = background.toColorOr(default = CellColors.DEFAULT.prizeInnerRing)
            .copy(alpha = 0.25f),
        highlightOverlay = CellColors.DEFAULT.highlightOverlay,
        selectedOverlay = CellColors.DEFAULT.selectedOverlay
    )
}

fun List<List<Int>>.toBoard(
    prizes: List<PrizeUiModel>,
    colors: CellColors,
): List<List<CellUiModel>> {
    return this.mapIndexed { rowIndex, row ->
        row.mapIndexed { colIndex, value ->
            CellUiModel(
                position = rowIndex to colIndex,
                value = value,
                prize = prizes.find { it.number == value },
                colors = colors,
            )
        }
    }
}

private fun CardsModel.Prize.toPrizeUi() = PrizeUiModel(
    id = id,
    title = title,
    amount = amount,
    type = type,
    number = number
)

private fun String?.toColorOr(default: Color): Color = try {
    if (this.isNullOrBlank()) default else Color(color = toColorInt())
} catch (_: Throwable) {
    default
}
