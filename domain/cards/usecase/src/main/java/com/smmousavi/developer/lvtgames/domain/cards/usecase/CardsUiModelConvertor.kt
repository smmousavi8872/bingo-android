package com.smmousavi.developer.lvtgames.domain.cards.usecase

import com.smmousavi.developer.lvtgames.core.model.domain.CardsModel
import com.smmousavi.developer.lvtgames.domain.cards.uimodel.CardsUiModel

fun CardsModel.asUiModel(): CardsUiModel = CardsUiModel(
    cards = cards.map { c ->
        CardsUiModel.CardUi(
            id = c.id,
            name = c.name,
            matrix = c.matrix,
            prizes = c.prizes.map { p ->
                CardsUiModel.PrizeUi(
                    id = p.id,
                    title = p.title,
                    amount = p.amount,
                    type = p.type,
                    number = p.number
                )
            },
            color = CardsUiModel.ColorUi(
                background = c.color.background,
                backgroundGradient1 = c.color.backgroundGradient1,
                backgroundGradient2 = c.color.backgroundGradient2,
                backgroundGradient3 = c.color.backgroundGradient3,
                titleColor = c.color.titleColor,
                textColor = c.color.textColor,
                borderColor = c.color.borderColor
            ),
            bet = c.bet
        )
    }
)