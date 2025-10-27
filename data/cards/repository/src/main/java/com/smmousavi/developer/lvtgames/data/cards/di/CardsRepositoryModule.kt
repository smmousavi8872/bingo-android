package com.smmousavi.developer.lvtgames.data.cards.di

import com.smmousavi.developer.lvtgames.data.cards.repository.DefaultCardsRepository
import org.koin.dsl.module

val cardsRepositoryModule = module {
    factory { DefaultCardsRepository(get()) }
}