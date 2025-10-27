package com.smmousavi.developer.lvtgames.bingo.di

import com.smmousavi.developer.lvtgames.bingo.BuildConfig
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single<String>(named("BASE_URL")) { BuildConfig.CARDS_BASE_URL }
    single<String>(named("DATABASE_NAME")) { BuildConfig.BINGO_DB_NAME }
    single<Json> {
        Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        }
    }
}