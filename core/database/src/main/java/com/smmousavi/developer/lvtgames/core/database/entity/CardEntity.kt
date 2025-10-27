package com.smmousavi.developer.lvtgames.core.database.entity


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards_table")
data class CardEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val matrixJson: String,
    val bet: Int,
    @Embedded(prefix = "color_")
    val color: ColorEmbeddedEntity,
) {
    data class ColorEmbeddedEntity(
        val background: String,
        val backgroundGradient1: String,
        val backgroundGradient2: String,
        val backgroundGradient3: String,
        val titleColor: String,
        val textColor: String,
        val borderColor: String,
    )
}

