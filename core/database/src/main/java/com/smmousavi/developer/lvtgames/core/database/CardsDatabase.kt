package com.smmousavi.developer.lvtgames.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.smmousavi.developer.lvtgames.core.database.dao.CardsRoomDao
import com.smmousavi.developer.lvtgames.core.database.entity.CardEntity
import com.smmousavi.developer.lvtgames.core.database.entity.PrizeEntity

@Database(
    entities = [CardEntity::class, PrizeEntity::class],
    version = 1,
    exportSchema = true
)
abstract class CardsDatabase : RoomDatabase() {
    abstract fun cardsDao(): CardsRoomDao
}