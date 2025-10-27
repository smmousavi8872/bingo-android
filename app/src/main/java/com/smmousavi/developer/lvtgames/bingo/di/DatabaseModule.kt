package com.smmousavi.developer.lvtgames.bingo.di

import androidx.room.Room
import com.smmousavi.developer.lvtgames.bingo.BuildConfig
import com.smmousavi.developer.lvtgames.core.database.CardsDatabase
import org.koin.dsl.module

import org.koin.android.ext.koin.androidContext

val databaseModule = module {
    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = CardsDatabase::class.java,
            name = BuildConfig.BINGO_DB_NAME
        )
            .fallbackToDestructiveMigration(dropAllTables = false) // todo: migration should be added
            .build()
    }
    single { get<CardsDatabase>().cardsDao() }
}