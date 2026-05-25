package com.smmousavi.developer.lvtgames.feature.cards.uimodel

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.smmousavi.developer.lvtgames.core.model.domain.cards.CardsModel
import kotlin.random.Random

fun CardsModel.asUiModel(): CardsListUiModel =
    CardsListUiModel(
        cards = cards.map { it.asUiModel() }
    )

fun CardsModel.Card.asUiModel(): CardUiModel {
    val cellColors: CellUiColors = colors.asCellColors()
    val prizeUiList: List<PrizeUiModel> = prizes.map { it.asPrizeUi() }

    return CardUiModel(
        id = id,
        name = name,
        board = matrix.asBoard(
            prizes = prizeUiList,
            colors = cellColors
        ),
        bet = bet,
        colors = CardUiColors(
            background = colors.background.asColorOr(default = Color.White),
            startGradient = colors.backgroundGradient1.asColorOr(default = CardUiColors.DEFAULT.startGradient),
            midGradient = colors.backgroundGradient2.asColorOr(default = CardUiColors.DEFAULT.midGradient),
            endGradient = colors.backgroundGradient3.asColorOr(default = CardUiColors.DEFAULT.endGradient),
            titleColor = colors.titleColor.asColorOr(default = Color.White),
            borderColor = colors.borderColor.asColorOr(CardUiColors.DEFAULT.borderColor)
        )
    )
}

private fun CardsModel.CardColors.asCellColors(): CellUiColors {
    val backgroundColor = background.asColorOr(default = CellUiColors.DEFAULT.background)
    val textColor = textColor.asColorOr(default = CellUiColors.DEFAULT.textOnValue)
    val borderColor = borderColor.asColorOr(default = CellUiColors.DEFAULT.prizeOuterRing)

    return CellUiColors(
        background = backgroundColor,
        textOnValue = textColor,
        textOnPrize = textColor,
        prizeOuterRing = borderColor,
        prizeInnerRing = backgroundColor,
        prizeInnerFill = backgroundColor.copy(alpha = 0.25f),
        highlightOverlay = CellUiColors.DEFAULT.highlightOverlay,
        selectedOverlay = CellUiColors.DEFAULT.selectedOverlay
    )
}

fun List<List<Int>>.asBoard(
    prizes: List<PrizeUiModel>,
    colors: CellUiColors,
): List<List<CellUiModel>> {
    // Build a lookup table once per board
    val prizeByNumber: Map<Int?, PrizeUiModel> = prizes.associateBy { it.number }

    return mapIndexed { rowIndex, row ->
        row.mapIndexed { colIndex, value ->
            CellUiModel(
                position = rowIndex to colIndex,
                value = value,
                prize = prizeByNumber[value],
                colors = colors
            )
        }
    }
}

fun CardsListUiModel.asDomainModel(): CardsModel =
    CardsModel(
        cards = cards.map { it.asDomainModel() }
    )

private fun CardsModel.Prize.asPrizeUi() = PrizeUiModel(
    id = id,
    cardId = cardId,
    title = title,
    amount = amount,
    type = type,
    number = number
)

fun CardUiModel.asDomainModel(): CardsModel.Card {
    return CardsModel.Card(
        id = id,
        name = name,
        matrix = board.asMatrix(),
        prizes = board.collectPrizes(),
        colors = colors.asDomainCardColors(board),
        bet = bet
    )
}

fun List<List<CellUiModel>>.collectPrizes(): List<CardsModel.Prize> {
    return flatMap { it }
        .mapNotNull { it.prize }
        .distinctBy { it.id }
        .map { it.asDomainPrize() }
}

fun PrizeUiModel.asDomainPrize(): CardsModel.Prize =
    CardsModel.Prize(
        id = id,
        cardId = cardId,
        title = title,
        amount = amount,
        type = type.orEmpty(),          // UI has nullable type, domain can be non-null
        number = number ?: -1           // or throw if you want it strictly non-null
    )

fun CardUiColors.asDomainCardColors(
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


fun CardsListUiModel.withPrize(prize: PrizeUiModel): CardsListUiModel =
    copy(
        cards = cards.map { card ->
            if (card.id != prize.cardId) card else card.withPrize(prize)
        }
    )

fun CardUiModel.withPrize(prize: PrizeUiModel): CardUiModel =
    copy(
        board = board.map { row ->
            row.map { cell ->
                if (cell.value == prize.number) {
                    cell.copy(prize = prize)
                } else {
                    cell
                }
            }
        }
    )

fun CardsListUiModel.withoutPrize(cardId: Int, prizeId: Int): CardsListUiModel =
    copy(
        cards = cards.map { card ->
            if (card.id != cardId) card else card.withoutPrize(prizeId)
        }
    )

fun CardUiModel.withoutPrize(prizeId: Int): CardUiModel =
    copy(
        board = board.map { row ->
            row.map { cell ->
                if (cell.prize?.id == prizeId) {
                    cell.copy(prize = null)
                } else {
                    cell
                }
            }
        }
    )

private fun Color.toHex(): String =
    "#%02X%02X%02X".format(
        (red * 255).toInt(),
        (green * 255).toInt(),
        (blue * 255).toInt()
    )

private fun List<List<CellUiModel>>.asMatrix(): List<List<Int>> =
    map { row ->
        row.map { cell -> cell.value }
    }

private fun String?.asColorOr(default: Color): Color = try {
    if (this.isNullOrBlank()) default else Color(color = toColorInt())
} catch (_: Throwable) {
    default
}