package com.smmousavi.developer.lvtgames.data.cards.datasource.remote

import com.smmousavi.developer.lvtgames.core.model.network.dto.CardsDto
import com.smmousavi.developer.lvtgames.core.network.CardsApiService
import kotlinx.serialization.InternalSerializationApi

@OptIn(InternalSerializationApi::class)
class DefaultCardsRemoteDataSource(val apiService: CardsApiService) : CardsRemoteDataSource {

    override suspend fun fetchCards(): Result<CardsDto> = runCatching {
        apiService.getCards()
    }
}