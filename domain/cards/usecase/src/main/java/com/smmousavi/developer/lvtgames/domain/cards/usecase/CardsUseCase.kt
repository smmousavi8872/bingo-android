package com.smmousavi.developer.lvtgames.domain.cards.usecase

import com.smmousavi.developer.lvtgames.domain.cards.uimodel.CardsUiModel

interface CardsUseCase {

    suspend operator fun invoke(): Result<List<CardsUiModel>>

}