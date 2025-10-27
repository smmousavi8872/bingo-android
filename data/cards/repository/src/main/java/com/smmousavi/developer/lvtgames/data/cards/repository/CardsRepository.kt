package com.smmousavi.developer.lvtgames.data.cards.repository

import com.smmousavi.developer.lvtgames.core.model.domain.CardsModel
import kotlinx.coroutines.flow.Flow

interface CardsRepository {
    suspend fun getCards(): Flow<Result<List<CardsModel>>>
}