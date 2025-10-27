package com.smmousavi.developer.lvtgames.core.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CardWithPrizesEntity(
    @Embedded val
    card: CardEntity,
    @Relation(parentColumn = "id", entityColumn = "cardId")
    val prizes: List<PrizeEntity>,
)