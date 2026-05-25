package com.smmousavi.developer.lvtgames.data.cards.offline.repository.offline

import com.smmousavi.developer.lvtgames.core.model.domain.cards.CardsModel
import kotlinx.coroutines.flow.Flow

interface CardsOfflineRepository {

    fun observeCards(): Flow<Result<CardsModel>>

    suspend fun addCardPrize(prize: CardsModel.Prize): Result<Unit>

    suspend fun deleteCardPrize(cardId: Int, prizeId: Int): Result<Unit>

    suspend fun refresh(): Result<Unit>
}