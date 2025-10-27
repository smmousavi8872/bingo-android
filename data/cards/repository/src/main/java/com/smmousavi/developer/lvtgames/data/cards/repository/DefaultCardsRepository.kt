package com.smmousavi.developer.lvtgames.data.cards.repository

import com.smmousavi.developer.lvtgames.core.model.domain.CardsModel
import com.smmousavi.developer.lvtgames.data.cards.datasource.remote.CardsRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class DefaultCardsRepository(val remoteDataSource: CardsRemoteDataSource) : CardsRepository {

    override suspend fun getCards(): Flow<Result<List<CardsModel>>> = flow {
        remoteDataSource.fetchCards()
        TODO("Not Implemented yet")
    }
}