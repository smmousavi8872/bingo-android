package com.smmousavi.developer.lvtgames.core.designsystem

/**
 * A generic UI state that represents the common phases of data loading state.
 *
 * @param T The type of data displayed in the UI.
 */
sealed interface UiState<out T> {

    data object Loading : UiState<Nothing>

    data class Success<T>(
        val data: T,
        val isRefreshing: Boolean = false,
        val errorMessage: String? = null,
    ) : UiState<T>

    data class Error(
        val message: String,
        val cause: Throwable? = null,
    ) : UiState<Nothing>
}