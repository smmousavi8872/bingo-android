package com.smmousavi.developer.lvtgames.data.cards.offline.repository.online

import com.smmousavi.developer.lvtgames.core.model.domain.cards.CardsModel
import com.smmousavi.developer.lvtgames.core.model.domain.cards.asDomainModel
import com.smmousavi.developer.lvtgames.data.cards.datasource.remote.CardsRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class DefaultCardsOnlineRepository(
    private val remoteDataSource: CardsRemoteDataSource,
) : CardsOnlineRepository {
    override fun fetchCards(): Flow<Result<CardsModel>> = flow {
        remoteDataSource.fetchCards().fold(
            onSuccess = {
                emit(Result.success(it.asDomainModel()))
            },
            onFailure = {
                emit(Result.failure(it))
            }
        )
    }
}