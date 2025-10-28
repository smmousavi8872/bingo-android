package com.smmousavi.developer.lvtgames.core.database

import com.smmousavi.developer.lvtgames.core.database.entity.CardEntity
import com.smmousavi.developer.lvtgames.core.database.entity.CardWithPrizesEntity
import com.smmousavi.developer.lvtgames.core.database.entity.PrizeEntity
import com.smmousavi.developer.lvtgames.core.model.database.dao.CardsDao
import com.smmousavi.developer.lvtgames.core.model.domain.cards.CardsModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun CardsModel.asCardEntity(json: Json): List<CardEntity> = cards.map { card ->
    CardEntity(
        id = card.id,
        name = card.name,
        matrixJson = json.encodeToString(card.matrix),
        bet = card.bet,
        color = CardEntity.ColorEmbeddedEntity(
            background = card.colors.background,
            backgroundGradient1 = card.colors.backgroundGradient1,
            backgroundGradient2 = card.colors.backgroundGradient2,
            backgroundGradient3 = card.colors.backgroundGradient3,
            titleColor = card.colors.titleColor,
            textColor = card.colors.textColor,
            borderColor = card.colors.borderColor
        )
    )
}

fun CardsModel.asPrizeEntity(): List<PrizeEntity> = cards.flatMap { card ->
    card.prizes.map { prize ->
        PrizeEntity(
            pk = 0,
            cardId = card.id,
            id = prize.id,
            title = prize.title,
            amount = prize.amount,
            type = prize.type,
            number = prize.number
        )
    }
}

fun List<CardWithPrizesEntity>.asCardsModel(json: Json): CardsModel = CardsModel(
    cards = this.map { cardPrize ->
        val matrix: List<List<Int>> = json.decodeFromString(cardPrize.card.matrixJson)
        CardsModel.Card(
            id = cardPrize.card.id,
            name = cardPrize.card.name,
            matrix = matrix,
            prizes = cardPrize.prizes.map { prize ->
                CardsModel.Prize(
                    id = prize.id,
                    title = prize.title,
                    amount = prize.amount,
                    type = prize.type,
                    number = prize.number
                )
            },
            colors = CardsModel.CardColors(
                background = cardPrize.card.color.background,
                backgroundGradient1 = cardPrize.card.color.backgroundGradient1,
                backgroundGradient2 = cardPrize.card.color.backgroundGradient2,
                backgroundGradient3 = cardPrize.card.color.backgroundGradient3,
                titleColor = cardPrize.card.color.titleColor,
                textColor = cardPrize.card.color.textColor,
                borderColor = cardPrize.card.color.borderColor,
            ),
            bet = cardPrize.card.bet
        )
    }
)

fun List<CardWithPrizesEntity>.asCardsDao(json: Json): CardsDao = CardsDao(
    cards = this.map { cw ->
        CardsDao.CardDao(
            id = cw.card.id,
            name = cw.card.name,
            matrix = json.decodeFromString(cw.card.matrixJson),
            prizes = cw.prizes.map { pr ->
                CardsDao.PrizeDao(
                    id = pr.id,
                    title = pr.title,
                    amount = pr.amount,
                    type = pr.type,
                    number = pr.number
                )
            },
            color = CardsDao.ColorDao(
                background = cw.card.color.background,
                backgroundGradient1 = cw.card.color.backgroundGradient1,
                backgroundGradient2 = cw.card.color.backgroundGradient2,
                backgroundGradient3 = cw.card.color.backgroundGradient3,
                titleColor = cw.card.color.titleColor,
                textColor = cw.card.color.textColor,
                borderColor = cw.card.color.borderColor
            ),
            bet = cw.card.bet
        )
    }
)