package com.smmousavi.developer.lvtgames.data.cards.datasource.remote

import com.smmousavi.developer.lvtgames.core.model.network.CardsDto
import com.smmousavi.developer.lvtgames.core.network.CardsApi

class DefaultCardsRemoteDataSource(val api: CardsApi) : CardsRemoteDataSource {
    override suspend fun fetchCards(): Result<CardsDto> = runCatching {
        api.getCards()
    }
}