package com.smmousavi.developer.lvtgames.domain.cards.usecase

import com.smmousavi.developer.lvtgames.data.cards.repository.CardsRepository
import com.smmousavi.developer.lvtgames.domain.cards.uimodel.CardsUiModel

class DefaultCardsUseCase(val repository: CardsRepository) : CardsUseCase {

    override suspend fun invoke(): Result<List<CardsUiModel>> {
        repository.getCards().collect {

        }
        TODO("Not yet implemented")
    }
}