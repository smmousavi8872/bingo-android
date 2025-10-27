package com.smmousavi.developer.lvtgames.core.model.network.dto

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@InternalSerializationApi
@Serializable
data class CardsDto(
    val cards: List<CardDto>? = null,
) {
    @Serializable
    data class CardDto(
        val id: Int? = null,
        val name: String? = null,
        val matrix: List<List<Int>?>? = null,
        val prizes: List<PrizeDto>? = null,
        val color: ColorDto? = null,
        val bet: Int? = null,
    )

    @Serializable
    data class PrizeDto(
        val id: Int? = null,
        val title: String? = null,
        val amount: Int? = null,
        val type: String? = null,
        val number: Int? = null,
    )

    @Serializable
    data class ColorDto(
        val background: String? = null,
        val backgroundGradient1: String? = null,
        val backgroundGradient2: String? = null,
        val backgroundGradient3: String? = null,
        val titleColor: String? = null,
        val textColor: String? = null,
        val borderColor: String? = null,
    )
}