package com.smmousavi.developer.lvtgames.bingo.di

import com.smmousavi.developer.lvtgames.bingo.BuildConfig
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    // expose BuildConfig.CARDS_BASE_URL to Retrofit module
    single(named("BASE_URL")) { BuildConfig.CARDS_BASE_URL }
}