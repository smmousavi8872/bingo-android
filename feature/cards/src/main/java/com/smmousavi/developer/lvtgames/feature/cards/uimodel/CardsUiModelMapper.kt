package com.smmousavi.developer.lvtgames.feature.cards.uimodel

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.smmousavi.developer.lvtgames.core.model.domain.cards.CardsModel
import kotlin.random.Random

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
        colors = CardUiColors(
            background = colors.background.toColorOr(default = Color.White),
            startGradient = colors.backgroundGradient1.toColorOr(default = CardUiColors.DEFAULT.startGradient),
            midGradient = colors.backgroundGradient2.toColorOr(default = CardUiColors.DEFAULT.midGradient),
            endGradient = colors.backgroundGradient3.toColorOr(default = CardUiColors.DEFAULT.endGradient),
            titleColor = colors.titleColor.toColorOr(default = Color.White),
            borderColor = colors.borderColor.toColorOr(CardUiColors.DEFAULT.borderColor)
        )
    )
}

private fun CardsModel.CardColors.toCellColors(): CellUiColors {
    return CellUiColors(
        background = background.toColorOr(default = CellUiColors.DEFAULT.background),
        textOnValue = textColor.toColorOr(default = CellUiColors.DEFAULT.textOnValue),
        textOnPrize = textColor.toColorOr(default = CellUiColors.DEFAULT.textOnPrize),
        prizeOuterRing = borderColor.toColorOr(default = CellUiColors.DEFAULT.prizeOuterRing),
        prizeInnerRing = background.toColorOr(default = CellUiColors.DEFAULT.prizeInnerRing),
        prizeInnerFill = background.toColorOr(default = CellUiColors.DEFAULT.prizeInnerRing)
            .copy(alpha = 0.25f),
        highlightOverlay = CellUiColors.DEFAULT.highlightOverlay,
        selectedOverlay = CellUiColors.DEFAULT.selectedOverlay
    )
}

fun List<List<Int>>.toBoard(
    prizes: List<PrizeUiModel>,
    colors: CellUiColors,
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

fun CardsUiModel.toDomainModel(): CardsModel =
    CardsModel(
        cards = cards.map { it.toDomainModel() }
    )

private fun CardsModel.Prize.toPrizeUi() = PrizeUiModel(
    id = id,
    cardId = cardId,
    title = title,
    amount = amount,
    type = type,
    number = number
)

fun CardUiModel.toDomainModel(): CardsModel.Card {
    return CardsModel.Card(
        id = id,
        name = name,
        matrix = board.toMatrix(),
        prizes = board.collectPrizes(),
        colors = colors.toDomainCardColors(board),
        bet = bet
    )
}

fun List<List<CellUiModel>>.collectPrizes(
): List<CardsModel.Prize> {
    return flatMap { it }
        .mapNotNull { it.prize }
        .distinctBy { it.id }
        .map { it.toDomainPrize() }
}

fun PrizeUiModel.toDomainPrize(): CardsModel.Prize =
    CardsModel.Prize(
        id = id,
        cardId = cardId,
        title = title,
        amount = amount,
        type = type.orEmpty(),          // UI has nullable type, domain can be non-null
        number = number ?: -1           // or throw if you want it strictly non-null
    )

fun CardUiColors.toDomainCardColors(
    board: List<List<CellUiModel>>,
): CardsModel.CardColors {
    // Try to use the first cell's textOnValue as the original textColor
    val sampleTextColor: Color? = board
        .asSequence()
        .flatMap { it.asSequence() }
        .firstOrNull()
        ?.colors
        ?.textOnValue

    return CardsModel.CardColors(
        background = background.toHex(),
        backgroundGradient1 = startGradient.toHex(),
        backgroundGradient2 = midGradient.toHex(),
        backgroundGradient3 = endGradient.toHex(),
        titleColor = titleColor.toHex(),
        textColor = (sampleTextColor ?: borderColor).toHex(), // fallback if needed
        borderColor = borderColor.toHex()
    )
}

fun newPrize(cardId: Int, number: Int) =
    PrizeUiModel(
        id = Random.nextInt(0, 100),
        cardId = cardId,
        title = "First Five",
        amount = 100,
        type = "cash",
        number = number
    )

private fun Color.toHex(): String =
    "#%02X%02X%02X".format(
        (red * 255).toInt(),
        (green * 255).toInt(),
        (blue * 255).toInt()
    )

private fun List<List<CellUiModel>>.toMatrix(): List<List<Int>> =
    map { row ->
        row.map { cell -> cell.value }
    }

private fun String?.toColorOr(default: Color): Color = try {
    if (this.isNullOrBlank()) default else Color(color = toColorInt())
} catch (_: Throwable) {
    default
}


