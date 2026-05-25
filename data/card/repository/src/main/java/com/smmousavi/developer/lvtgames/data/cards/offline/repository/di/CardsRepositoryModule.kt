package com.smmousavi.developer.lvtgames.data.cards.offline.repository.di

import com.smmousavi.developer.lvtgames.data.cards.offline.repository.offline.CardsOfflineRepository
import com.smmousavi.developer.lvtgames.data.cards.offline.repository.offline.DefaultCardsOfflineRepository
import com.smmousavi.developer.lvtgames.data.cards.offline.repository.online.CardsOnlineRepository
import com.smmousavi.developer.lvtgames.data.cards.offline.repository.online.DefaultCardsOnlineRepository
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val cardsOfflineRepositoryModule = module {
    single<CardsOfflineRepository> {
        DefaultCardsOfflineRepository(
            localDataSource = get(),
            remoteDataSource = get(),
            dispatcher = Dispatchers.IO
        )
    }

    single<CardsOnlineRepository> {
        DefaultCardsOnlineRepository(
            remoteDataSource = get(),
        )
    }
}