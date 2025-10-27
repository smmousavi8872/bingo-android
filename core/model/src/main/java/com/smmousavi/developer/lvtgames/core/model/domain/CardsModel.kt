package com.smmousavi.developer.lvtgames.core.model.domain

data class CardsModel(
    val cards: List<Card>
) {
    data class Card(
        val id: Int,
        val name: String,
        val matrix: List<List<Int>>, //-1 = empty
        val prizes: List<Prize>,
        val color: Color,
        val bet: Int,
    )
    data class Prize(
        val id: Int,
        val title: String,
        val amount: Int,
        val type: String,
        val number: Int
    )
    data class Color(
        val background: String,
        val backgroundGradient1: String,
        val backgroundGradient2: String,
        val backgroundGradient3: String,
        val titleColor: String,
        val textColor: String,
        val borderColor: String
    )
}