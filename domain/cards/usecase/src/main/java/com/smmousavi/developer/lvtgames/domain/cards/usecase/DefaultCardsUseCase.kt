package com.smmousavi.developer.lvtgames.domain.cards.usecase

import com.smmousavi.developer.lvtgames.core.model.domain.CardsModel
import com.smmousavi.developer.lvtgames.data.cards.repository.CardsRepository
import com.smmousavi.developer.lvtgames.domain.cards.uimodel.CardsUiModel
import com.smmousavi.developer.lvtgames.domain.cards.uimodel.asUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map


class DefaultCardsUseCase(val repository: CardsRepository) : CardsUseCase {

    override suspend fun invoke(): Flow<Result<CardsUiModel>> =
        repository.observeCards()
            .map { result: Result<CardsModel> ->
                result.map { domain -> domain.asUiModel() }
            }
            .catch { e -> emit(Result.failure(e)) }

    override suspend fun refresh(): Result<Unit> = repository.refresh()
}