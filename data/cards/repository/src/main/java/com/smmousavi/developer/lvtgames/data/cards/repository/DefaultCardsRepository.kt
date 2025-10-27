package com.smmousavi.developer.lvtgames.data.cards.repository

import com.smmousavi.developer.lvtgames.core.model.domain.CardsModel
import kotlinx.coroutines.flow.Flow


class DefaultCardsRepository : CardsRepository {

    override suspend fun getCards(): Flow<Result<List<CardsModel>>> {
        TODO("Not yet implemented")
    }
}