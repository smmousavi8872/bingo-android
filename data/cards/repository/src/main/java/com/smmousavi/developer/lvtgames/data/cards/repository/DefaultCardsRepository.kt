package com.smmousavi.developer.lvtgames.data.cards.repository

import com.smmousavi.developer.lvtgames.core.model.domain.CardsModel
import com.smmousavi.developer.lvtgames.core.model.domain.asDomainModel
import com.smmousavi.developer.lvtgames.data.cards.datasource.local.CardsLocalDataSource
import com.smmousavi.developer.lvtgames.data.cards.datasource.remote.CardsRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.withContext


class DefaultCardsRepository(
    private val localDataSource: CardsLocalDataSource,
    private val remoteDataSource: CardsRemoteDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : CardsRepository {

    /**
     * DefaultCardsRepository
     *
     * Implements an offline-first strategy:
     *   - Always emits cached data from Room first
     *   - Then performs a one-shot network refresh
     *   - On successful fetch, caches the fresh data, which re-emits automatically
     *   - Any errors are surfaced as Result.failure
     */
    override fun observeCards(): Flow<Result<CardsModel>> {
        // Streams cards from local DB and wraps them in Result.success.
        val cacheFlow: Flow<Result<CardsModel>> = localDataSource.observe()
            .distinctUntilChanged()
            .map { Result.success(it) }

        // Executes a single remote fetch when collection starts.
        // Emits Result<CardsModel> immediately so UI can handle success/error
        val refreshFlow: Flow<Result<CardsModel>> = flow {
            val result = withContext(dispatcher) {
                remoteDataSource.fetchCards()
                    .mapCatching { it.asDomainModel() }
            }

            // Emit the result success or failur
            emit(result)

            // In case of success upsert the database
            result.onSuccess { model ->
                withContext(dispatcher) {
                    localDataSource.upsert(model).getOrThrow()
                }
            }
        }

        // Cache emits immediately, then network, then updated cache.
        return merge(cacheFlow, refreshFlow)
            .catch { e -> emit(Result.failure(e)) } // for unexpected exceptions
            .flowOn(dispatcher)
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