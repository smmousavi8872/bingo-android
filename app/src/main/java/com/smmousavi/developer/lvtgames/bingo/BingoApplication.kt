package com.smmousavi.developer.lvtgames.bingo

import android.app.Application
import com.smmousavi.developer.lvtgames.bingo.di.appModule
import com.smmousavi.developer.lvtgames.bingo.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class BingoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BingoApplication)
            modules(
                appModule,
                networkModule,
            )
        }
    }
}