package com.smmousavi.developer.lvtgames.domain.cards.usecase

import com.smmousavi.developer.lvtgames.domain.cards.uimodel.CardsUiModel

class DefaultCardsUseCase : CardsUseCase {
    override suspend fun invoke(): Result<List<CardsUiModel>> {
        TODO("Not yet implemented")
    }
}