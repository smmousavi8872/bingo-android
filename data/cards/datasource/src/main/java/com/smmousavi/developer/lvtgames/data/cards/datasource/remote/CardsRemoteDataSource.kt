package com.smmousavi.developer.lvtgames.data.cards.datasource.remote

import com.smmousavi.developer.lvtgames.core.model.network.CardsDto

interface CardsRemoteDataSource {
    suspend fun fetchCards(): Result<CardsDto>
}