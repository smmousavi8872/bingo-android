package com.smmousavi.developer.lvtgames.domain.cards.usecase

import com.smmousavi.developer.lvtgames.core.model.domain.cards.CardsModel
import kotlinx.coroutines.flow.Flow

interface CardsUseCase {
    suspend operator fun invoke(): Flow<Result<CardsModel>>

    suspend fun addCardPrize(prize: CardsModel.Prize): Result<Unit>

    suspend fun deleteCardPrize(cardId: Int, prizeId: Int): Result<Unit>

    suspend fun refresh(): Result<Unit>

}