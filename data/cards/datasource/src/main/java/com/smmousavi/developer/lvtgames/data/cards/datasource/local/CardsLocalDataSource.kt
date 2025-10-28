package com.smmousavi.developer.lvtgames.data.cards.datasource.local

import com.smmousavi.developer.lvtgames.core.model.domain.cards.CardsModel
import kotlinx.coroutines.flow.Flow

interface CardsLocalDataSource {

    fun observe(): Flow<CardsModel>
    
    suspend fun upsert(snapshot: CardsModel): Result<Unit>
}