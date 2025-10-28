package com.smmousavi.developer.lvtgames.feature.cards.uimodel

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.smmousavi.developer.lvtgames.core.model.domain.CardsModel
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.PieceColors.Companion.DEFAULT

fun CardsModel.toUiModel(): CardsUiModel =
    CardsUiModel(cards = cards.map { it.toUiModel() })

fun CardsModel.Card.toUiModel(): CardUiModel {
    return CardUiModel(
        id = id,
        name = name,
        matrix = matrix,
        prizes = prizes.map { it.toPrizeUi() },
        bet = bet,
        pieceColors = color.toPieceColors(),
        background = color.toCardChrome().first,
        backgroundGradient1 = color.toCardChrome().second.first,
        backgroundGradient2 = color.toCardChrome().second.second,
        backgroundGradient3 = color.toCardChrome().second.third,
        titleColor = color.titleColor.toColorOr(Color.White),
        textColor = color.textColor.toColorOr(Color(0xFF1B1B1B)),
        borderColor = color.borderColor.toColorOr(Color(0xFF444444)),
    )
}
private fun CardsModel.Color?.toPieceColors(): PieceColors {
    return PieceColors(
        primaryCell = this?.background.toColorOr(DEFAULT.primaryCell),
        secondaryCell = DEFAULT.secondaryCell,
        textOnPrimary = this?.textColor.toColorOr(DEFAULT.textOnPrimary),
        textOnSecondary = DEFAULT.textOnSecondary,
        tokenOuterRing = this?.borderColor.toColorOr(DEFAULT.tokenOuterRing),
        tokenInnerRing = this?.borderColor.toColorOr(DEFAULT.tokenOuterRing).copy(alpha = 0.85f),
        tokenInnerFill = DEFAULT.tokenInnerFill,
        highlightOverlay = DEFAULT.highlightOverlay,
        selectedOverlay = DEFAULT.selectedOverlay
    )
}

private fun CardsModel.Color?.toCardChrome(): Pair<Color, Triple<Color?, Color?, Color?>> {
    val background = this?.background.toColorOr(Color(0xFF2B2B2B))
    val gradient1 = this?.backgroundGradient1?.toColorOr(background)
    val gradient2 = this?.backgroundGradient2?.toColorOr(background)
    val gradient3 = this?.backgroundGradient3?.toColorOr(background)
    return Pair(background, Triple(gradient1, gradient2, gradient3))
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
