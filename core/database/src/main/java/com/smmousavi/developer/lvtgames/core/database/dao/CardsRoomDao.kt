package com.smmousavi.developer.lvtgames.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.smmousavi.developer.lvtgames.core.database.entity.CardEntity
import com.smmousavi.developer.lvtgames.core.database.entity.CardWithPrizesEntity
import com.smmousavi.developer.lvtgames.core.database.entity.PrizeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CardsRoomDao {

    @Transaction
    @Query("SELECT * FROM cards_table ORDER BY id ASC")
    fun observeCards(): Flow<List<CardWithPrizesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCards(cards: List<CardEntity>)

    @Query("DELETE FROM prizes_table WHERE cardId = :cardId")
    suspend fun deletePrizesFor(cardId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrizes(prizes: List<PrizeEntity>)

    @Query("DELETE FROM cards_table")
    suspend fun clearCards()

    @Query("DELETE FROM prizes_table")
    suspend fun clearPrizes()
}