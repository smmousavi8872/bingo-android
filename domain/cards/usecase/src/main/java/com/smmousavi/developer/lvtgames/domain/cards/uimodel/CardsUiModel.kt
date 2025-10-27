package com.smmousavi.developer.lvtgames.domain.cards.uimodel

data class CardsUiModel(
    var cards: List<CardUi>? = null,
) {
    data class CardUi(
        var id: Int? = null,
        var name: String? = null,
        var matrix: List<List<Int>>? = null,
        var prizes: List<PrizeUi>? = null,
        var color: ColorUi? = null,
        var bet: Int? = null,
    )

    data class PrizeUi(
        var id: Int? = null,
        var title: String? = null,
        var amount: Int? = null,
        var type: String? = null,
        var number: Int? = null,
    )

    data class ColorUi(
        var background: String? = null,
        var backgroundGradient1: String? = null,
        var backgroundGradient2: String? = null,
        var backgroundGradient3: String? = null,
        var titleColor: String? = null,
        var textColor: String? = null,
        var borderColor: String? = null,
    )
}