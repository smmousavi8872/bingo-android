package com.smmousavi.developer.lvtgames.core.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@InternalSerializationApi
@Serializable
data class CardColor(
    val background: String,
    val backgroundGradient1: String,
    val backgroundGradient2: String,
    val backgroundGradient3: String,
    val titleColor: String,
    val textColor: String,
    val borderColor: String,
)

@InternalSerializationApi
@Serializable
data class Prize(
    val id: Int,
    val title: String,
    val amount: Int,
    val type: String,
    val number: Int,
)

@InternalSerializationApi
@Serializable
data class Card(
    val id: Int, val name: String,
    val matrix: List<List<Int>>,
    val prizes: List<Prize>,
    val color: CardColor,
    val bet: Int? = null,
)

@InternalSerializationApi
@Serializable
data class CardsPayload(val cards: List<Card>)