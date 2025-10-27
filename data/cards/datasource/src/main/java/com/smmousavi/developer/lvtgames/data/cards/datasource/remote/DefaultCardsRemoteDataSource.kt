package com.smmousavi.developer.lvtgames.data.cards.datasource.remote

import com.smmousavi.developer.lvtgames.core.model.network.CardsDto

class DefaultCardsRemoteDataSource : CardsRemoteDataSource {
    override suspend fun fetchCards(): Result<List<CardsDto>> {
        TODO("Not yet implemented")
    }
}