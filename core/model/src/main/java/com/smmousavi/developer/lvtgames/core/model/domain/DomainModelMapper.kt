package com.smmousavi.developer.lvtgames.core.model.domain

import com.smmousavi.developer.lvtgames.core.model.network.dto.CardsDto
import kotlinx.serialization.InternalSerializationApi


@OptIn(InternalSerializationApi::class)
fun CardsDto.asDomainModel(): CardsModel {
    val safeCards = (cards ?: emptyList()).map { card ->
        val safeMatrix = (card.matrix ?: listOf(listOf(), listOf(), listOf()))
            .map { row -> (row ?: emptyList()) }

        CardsModel.Card(
            id = card.id ?: 0,
            name = card.name ?: "Card",
            matrix = safeMatrix,
            prizes = (card.prizes ?: emptyList()).map { prize ->
                CardsModel.Prize(
                    id = prize.id ?: 0,
                    title = prize.title ?: "",
                    amount = prize.amount ?: 0,
                    type = prize.type ?: "",
                    number = prize.number ?: -1
                )
            },
            color = card.color.let { c ->
                CardsModel.Color(
                    background = c?.background ?: "#054A29",
                    backgroundGradient1 = c?.backgroundGradient1 ?: "#0A5B30",
                    backgroundGradient2 = c?.backgroundGradient2 ?: "#0F6C38",
                    backgroundGradient3 = c?.backgroundGradient3 ?: "#137E40",
                    titleColor = c?.titleColor ?: "#FFFFFF",
                    textColor = c?.textColor ?: "#FFFFFF",
                    borderColor = c?.borderColor ?: "#FFFFFF"
                )
            },
            bet = card.bet ?: 0
        )
    }
    return CardsModel(cards = safeCards)
}