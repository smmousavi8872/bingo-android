package com.smmousavi.developer.lvtgames.domain.cards.usecase

import com.smmousavi.developer.lvtgames.core.model.domain.CardsModel
import kotlinx.coroutines.flow.Flow

interface CardsUseCase {
    suspend operator fun invoke(): Flow<Result<CardsModel>>

    suspend fun refresh(): Result<Unit>

}