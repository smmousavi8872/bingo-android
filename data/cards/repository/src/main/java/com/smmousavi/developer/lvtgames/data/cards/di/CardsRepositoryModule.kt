package com.smmousavi.developer.lvtgames.data.cards.di

import com.smmousavi.developer.lvtgames.data.cards.repository.CardsRepository
import com.smmousavi.developer.lvtgames.data.cards.repository.DefaultCardsRepository
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val cardsRepositoryModule = module {
    single<CardsRepository> {
        DefaultCardsRepository(
            localDataSource = get(),
            remoteDataSource = get(),
            dispatcher = Dispatchers.IO
        )
    }
}