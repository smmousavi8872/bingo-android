package com.smmousavi.developer.lvtgames.data.cards.datasource.di

import com.smmousavi.developer.lvtgames.data.cards.datasource.local.CardsLocalDataSource
import com.smmousavi.developer.lvtgames.data.cards.datasource.local.DefaultCardsLocalDataSource
import com.smmousavi.developer.lvtgames.data.cards.datasource.remote.CardsRemoteDataSource
import com.smmousavi.developer.lvtgames.data.cards.datasource.remote.DefaultCardsRemoteDataSource
import org.koin.dsl.module


val cardsDataSourceModule = module {
    single<CardsRemoteDataSource> { DefaultCardsRemoteDataSource(apiService = get()) }

    single<CardsLocalDataSource> { DefaultCardsLocalDataSource(dao = get(), json = get()) }
}