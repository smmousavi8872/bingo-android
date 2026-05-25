package com.smmousavi.developer.lvtgames.data.cards.offline.repository.online

import com.smmousavi.developer.lvtgames.core.model.domain.cards.CardsModel
import kotlinx.coroutines.flow.Flow

interface CardsOnlineRepository {

    fun fetchCards(): Flow<Result<CardsModel>>

}