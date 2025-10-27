package com.smmousavi.developer.lvtgames.core.database.entity


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "prizes_table",
    foreignKeys = [
        ForeignKey(
            entity = CardEntity::class,
            parentColumns = ["id"],
            childColumns = ["cardId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["cardId"])]
)
data class PrizeEntity(
    @PrimaryKey(autoGenerate = true)
    val pk: Int = 0,
    val cardId: Int,
    val id: Int,
    val title: String,
    val amount: Int,
    val type: String,
    val number: Int,
)