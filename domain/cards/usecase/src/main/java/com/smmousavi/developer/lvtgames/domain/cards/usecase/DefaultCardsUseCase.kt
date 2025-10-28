package com.smmousavi.developer.lvtgames.domain.cards.usecase

import com.smmousavi.developer.lvtgames.core.model.domain.cards.CardsModel
import com.smmousavi.developer.lvtgames.data.cards.repository.CardsRepository
import kotlinx.coroutines.flow.Flow


class DefaultCardsUseCase(val repository: CardsRepository) : CardsUseCase {

    override suspend fun invoke(): Flow<Result<CardsModel>> = repository.observeCards()

    override suspend fun refresh(): Result<Unit> = repository.refresh()
}