package com.smmousavi.developer.lvtgames.data.cards.datasource.remote

import com.smmousavi.developer.lvtgames.core.model.network.dto.CardsDto
import kotlinx.serialization.InternalSerializationApi

@OptIn(InternalSerializationApi::class)
interface CardsRemoteDataSource {
    suspend fun fetchCards(): Result<CardsDto>
}