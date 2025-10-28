package com.smmousavi.developer.lvtgames.data.cards.datasource.local

import com.smmousavi.developer.lvtgames.core.database.asCardEntity
import com.smmousavi.developer.lvtgames.core.database.asCardsModel
import com.smmousavi.developer.lvtgames.core.database.asPrizeEntity
import com.smmousavi.developer.lvtgames.core.database.dao.CardsRoomDao
import com.smmousavi.developer.lvtgames.core.model.domain.cards.CardsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class DefaultCardsLocalDataSource(
    private val dao: CardsRoomDao,
    private val json: Json,
) : CardsLocalDataSource {
    /**
     * Observes all cached cards as a stream of domain models.
     * Emits updates only when actual data content changes.
     */
    override fun observe(): Flow<CardsModel> = dao.observeCards()
        .distinctUntilChanged()
        .map { cardPrizes ->
            cardPrizes.asCardsModel(json)
        }

    /**
     * Replaces all cached cards and prizes with the new snapshot,
     * wrapped in [Result] for safe use at higher layers.
     */
    override suspend fun upsert(snapshot: CardsModel): Result<Unit> = runCatching {
        val cards = snapshot.asCardEntity(json)
        val prizes = snapshot.asPrizeEntity()

        // clear cards table
        dao.clearCards()
        dao.upsertCards(cards)

        // clear prizes table
        dao.clearPrizes()
        dao.insertPrizes(prizes)
    }
}