package com.smmousavi.developer.lvtgames.feature.cards

import androidx.lifecycle.ViewModel
import com.smmousavi.developer.lvtgames.domain.cards.usecase.CardsUseCase

class CardsViewModel(val cardsUseCase: CardsUseCase) : ViewModel() {

    suspend fun getCards() {
        cardsUseCase.invoke()
        TODO("Not Implemented yet")
    }
}