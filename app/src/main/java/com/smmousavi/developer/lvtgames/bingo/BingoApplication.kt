package com.smmousavi.developer.lvtgames.bingo

import android.app.Application
import com.smmousavi.developer.lvtgames.bingo.di.appModule
import com.smmousavi.developer.lvtgames.bingo.di.networkModule
import com.smmousavi.developer.lvtgames.data.cards.datasource.di.cardsDataSourceModule
import com.smmousavi.developer.lvtgames.data.cards.di.cardsRepositoryModule
import com.smmousavi.developer.lvtgames.domain.cards.di.cardsUseCaseModule
import com.smmousavi.developer.lvtgames.feature.cards.di.cardsViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class BingoApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize koin modules
        startKoin {
            androidContext(this@BingoApplication)
            modules(
                appModule,
                networkModule,
                cardsDataSourceModule,
                cardsRepositoryModule,
                cardsUseCaseModule,
                cardsViewModelModule,
            )
        }
    }
}