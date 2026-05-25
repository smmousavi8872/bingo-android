package com.smmousavi.developer.lvtgames.domain.cards.usecase

import com.smmousavi.developer.lvtgames.core.model.domain.cards.CardsModel
import com.smmousavi.developer.lvtgames.data.cards.offline.repository.offline.CardsOfflineRepository
import kotlinx.coroutines.flow.Flow


class DefaultCardsUseCase(val repository: CardsOfflineRepository) : CardsUseCase {

    override suspend fun invoke(): Flow<Result<CardsModel>> = repository.observeCards()

    override suspend fun addCardPrize(prize: CardsModel.Prize): Result<Unit> =
        repository.addCardPrize(prize)

    override suspend fun deleteCardPrize(
        cardId: Int,
        prizeId: Int,
    ): Result<Unit> =
        repository.deleteCardPrize(cardId = cardId, prizeId = prizeId)

    override suspend fun refresh(): Result<Unit> = repository.refresh()
}