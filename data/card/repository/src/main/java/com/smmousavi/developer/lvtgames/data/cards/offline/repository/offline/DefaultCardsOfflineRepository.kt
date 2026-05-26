package com.smmousavi.developer.lvtgames.data.cards.offline.repository.offline

import com.smmousavi.developer.lvtgames.core.model.domain.cards.CardsModel
import com.smmousavi.developer.lvtgames.core.model.domain.cards.asDomainModel
import com.smmousavi.developer.lvtgames.data.cards.datasource.local.CardsLocalDataSource
import com.smmousavi.developer.lvtgames.data.cards.datasource.remote.CardsRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext


class DefaultCardsOfflineRepository(
    private val localDataSource: CardsLocalDataSource,
    private val remoteDataSource: CardsRemoteDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : CardsOfflineRepository {

    /**
     * DefaultCardsRepository
     *
     * Implements an offline-first strategy:
     *   - Always emits cached data from Room first
     *   - Then performs a one-shot network refresh
     *   - On successful fetch, caches the fresh data, which re-emits automatically
     *   - Any errors are surfaced as Result.failure
     */
    override fun observeCards(): Flow<Result<CardsModel>> = merge(
        // Local cache → Result.success, deduped
        localDataSource.loadCards()
            .distinctUntilChanged()
            .map { Result.success(it) },
        // Single refresh per collector: fetch → emit Result → upsert on success
        flowOf(Unit).transform { _ ->
            val result = remoteDataSource.fetchCards()
                .mapCatching { it.asDomainModel() }
            emit(result)
            result.onSuccess { model ->
                localDataSource.upsert(model).getOrThrow()
            }
        })

    override suspend fun addCardPrize(prize: CardsModel.Prize): Result<Unit> = runCatching {
        localDataSource.insertCellPrize(prize)
    }

    override suspend fun deleteCardPrize(
        cardId: Int,
        prizeId: Int,
    ): Result<Unit> = runCatching {
        localDataSource.deleteCellPrize(cardId, prizeId)
    }

    /**
     * Fetches new data from network, updates cache, returns success/failure.
     */
    override suspend fun refresh(): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.fetchCards()
            .mapCatching { it.asDomainModel() }
            .onSuccess { model ->
                localDataSource.upsert(model).getOrThrow()
            }
            .map { }
    }
}