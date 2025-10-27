package com.smmousavi.developer.lvtgames.core.model.database.dao

data class CardsDao(
    val cards: List<CardDao>,
) {
    data class CardDao(
        val id: Int,
        val name: String,
        val matrix: List<List<Int>>,
        val prizes: List<PrizeDao>,
        val color: ColorDao,
        val bet: Int,
    )

    data class PrizeDao(
        val id: Int,
        val title: String,
        val amount: Int,
        val type: String,
        val number: Int,
    )

    data class ColorDao(
        val background: String,
        val backgroundGradient1: String,
        val backgroundGradient2: String,
        val backgroundGradient3: String,
        val titleColor: String,
        val textColor: String,
        val borderColor: String,
    )
}