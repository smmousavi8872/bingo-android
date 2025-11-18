package com.smmousavi.developer.lvtgames.feature.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smmousavi.developer.lvtgames.core.designsystem.UiState
import com.smmousavi.developer.lvtgames.core.model.domain.cards.CardsModel
import com.smmousavi.developer.lvtgames.domain.cards.usecase.CardsUseCase
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.CardsUiModel
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.PrizeUiModel
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.asDomainPrize
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.asUiModel
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.withPrize
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.withoutPrize
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CardsViewModel(
    private val useCase: CardsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<CardsUiModel>>(UiState.Loading)
    val state: StateFlow<UiState<CardsUiModel>> = _state.asStateFlow()

    // Keep the last domain snapshot to avoid remapping identical data
    private var latestDomain: CardsModel? = null

    // Prevent multiple collectors if observeCards() is called more than once
    private var observing = false

    fun observeCards() {
        if (observing) return
        observing = true

        viewModelScope.launch {
            useCase().collect { result ->
                result
                    .onSuccess { data ->
                        if (data.cards.isEmpty()) return@onSuccess

                        // Skip if domain data didn't actually change
                        if (latestDomain == data && _state.value is UiState.Success) return@onSuccess
                        latestDomain = data

                        val uiModel = data.asUiModel()
                        _state.value = UiState.Success(uiModel)
                    }
                    .onFailure { e ->
                        _state.value = UiState.Error(e.message ?: "Unknown error", e)
                    }
            }
        }
    }

    /**
     * Optimistic UI update + persist through use case.
     * We first update the current UiState, then trigger domain update.
     */
    fun addCardPrize(prize: PrizeUiModel) {
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

    private inline fun updateUi(transform: (CardsUiModel) -> CardsUiModel) {
        val current = _state.value
        if (current is UiState.Success) {
            _state.value = UiState.Success(transform(current.data))
        }
    }
}


