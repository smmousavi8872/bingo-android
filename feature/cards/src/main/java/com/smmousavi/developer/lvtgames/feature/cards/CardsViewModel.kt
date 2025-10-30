package com.smmousavi.developer.lvtgames.feature.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smmousavi.developer.lvtgames.core.designsystem.UiState
import com.smmousavi.developer.lvtgames.core.model.domain.cards.CardsModel
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.CardsUiModel
import com.smmousavi.developer.lvtgames.domain.cards.usecase.CardsUseCase
import com.smmousavi.developer.lvtgames.feature.cards.uimodel.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.*

class CardsViewModel(
    private val useCase: CardsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<CardsUiModel>>(UiState.Loading)
    val state: StateFlow<UiState<CardsUiModel>> = _state.asStateFlow()

    fun observeCards() {
        viewModelScope.launch {
            useCase().collect { result ->
                result
                    .onSuccess { data ->
                        if (data.cards.isNotEmpty()) _state.value =
                            UiState.Success(data.toUiModel())
                    }
                    .onFailure { e ->
                        _state.value = UiState.Error(e.message ?: "Unknown error", e)
                    }
            }
        }
    }
}