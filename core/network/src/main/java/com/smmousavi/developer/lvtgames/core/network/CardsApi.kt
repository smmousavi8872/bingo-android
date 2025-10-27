package com.smmousavi.developer.lvtgames.core.network

import com.smmousavi.developer.lvtgames.core.model.network.CardsDto
import kotlinx.serialization.InternalSerializationApi
import retrofit2.http.GET


interface CardsApi {
    @OptIn(InternalSerializationApi::class)
    @GET("cards")
    suspend fun getCards(): CardsDto
}