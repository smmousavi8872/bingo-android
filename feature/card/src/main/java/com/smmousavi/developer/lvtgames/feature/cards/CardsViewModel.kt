package com.smmousavi.developer.lvtgames.feature.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smmousavi.developer.lvtgames.core.designsystem.UiState
import com.smmousavi.developer.lvtgames.domain.cards.usecase.CardsUseCase
import com.smmousavi.developer.lvtgames.feature.cards.uistate.CardsListUiState
import com.smmousavi.developer.lvtgames.feature.cards.uistate.PrizeUiState
import com.smmousavi.developer.lvtgames.feature.cards.uistate.asDomainPrize
import com.smmousavi.developer.lvtgames.feature.cards.uistate.asUiModel
import com.smmousavi.developer.lvtgames.feature.cards.uistate.withPrize
import com.smmousavi.developer.lvtgames.feature.cards.uistate.withoutPrize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

class CardsViewModel(
    private val useCase: CardsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<CardsListUiState>>(UiState.Loading)
    val state: StateFlow<UiState<CardsListUiState>> = _state.asStateFlow()

    fun observeCards() {
        viewModelScope.launch {
            useCase().collect { result ->
                result
                    .onSuccess { data ->
                        val uiModel = data.asUiModel()
                        _state.value = UiState.Success(uiModel)
                    }
                    .onFailure { e ->
                        _state.value = UiState.Error(e.message ?: "Unknown error", e)
                    }
            }

            useCase().first()
            useCase().single()
            useCase().launchIn(this)
        }
    }

    /**
     * Optimistic UI update + persist through use case.
     * First update the current UiState, then trigger domain update.
     */
    fun addCardPrize(prize: PrizeUiState) {
        // Optimistic UI update
        updateUi { it.withPrize(prize) }

        // Persist on domain layer
        viewModelScope.launch {
            useCase.addCardPrize(prize.asDomainPrize())
        }
    }

    fun deleteCardPrize(cardId: Int, prizeId: Int) {
        // Optimistic UI update
        updateUi { it.withoutPrize(cardId, prizeId) }

        // Persist on domain layer
        viewModelScope.launch {
            useCase.deleteCardPrize(cardId = cardId, prizeId = prizeId)
        }
    }

    private inline fun updateUi(transform: (CardsListUiState) -> CardsListUiState) {
        val current = _state.value
        if (current is UiState.Success) {
            _state.value = UiState.Success(transform(current.data))
        }
    }
}


