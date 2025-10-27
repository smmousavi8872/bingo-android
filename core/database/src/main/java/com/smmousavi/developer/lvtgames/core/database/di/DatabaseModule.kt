package com.smmousavi.developer.lvtgames.core.database.di

import androidx.room.Room
import com.smmousavi.developer.lvtgames.core.database.CardsDatabase
import com.smmousavi.developer.lvtgames.core.database.dao.CardsRoomDao
import org.koin.dsl.module

import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named

val databaseModule = module {
    single<CardsDatabase> {
        val dbName = get<String>(named("DATABASE_NAME")) // provided in app module
        Room.databaseBuilder(
            context = androidContext(),
            klass = CardsDatabase::class.java,
            name = dbName
        )
            .fallbackToDestructiveMigration() // todo: migration should be added
            .build()
    }

    single<CardsRoomDao> { get<CardsDatabase>().cardsDao() }
}