package com.smmousavi.developer.lvtgames.data.cards.datasource.di

import com.smmousavi.developer.lvtgames.data.cards.datasource.remote.DefaultCardsRemoteDataSource
import org.koin.dsl.module


val cardsDataSourceModule = module {
    factory { DefaultCardsRemoteDataSource(get()) }
}