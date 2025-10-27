package com.smmousavi.developer.lvtgames.domain.cards.di

import com.smmousavi.developer.lvtgames.domain.cards.usecase.DefaultCardsUseCase
import org.koin.dsl.module

val cardsUseCaseModule = module {

    factory { DefaultCardsUseCase(get()) }

}