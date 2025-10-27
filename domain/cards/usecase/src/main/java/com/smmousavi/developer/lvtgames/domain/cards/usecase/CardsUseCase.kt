package com.smmousavi.developer.lvtgames.domain.cards.usecase

import com.smmousavi.developer.lvtgames.domain.cards.uimodel.CardsUiModel
import kotlinx.coroutines.flow.Flow

interface CardsUseCase {
    suspend operator fun invoke(): Flow<Result<CardsUiModel>>

    suspend fun refresh(): Result<Unit>

}