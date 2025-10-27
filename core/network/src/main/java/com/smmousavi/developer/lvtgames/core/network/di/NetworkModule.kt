package com.smmousavi.developer.lvtgames.core.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.smmousavi.developer.lvtgames.core.network.CardsApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit


val networkModule = module {
    single<OkHttpClient> {
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .apply {
                addInterceptor(
                    HttpLoggingInterceptor().setLevel(
                        HttpLoggingInterceptor.Level.BASIC
                    )
                )
            }
            .build()
    }

    single<CardsApiService> {
        val baseUrl = get<String>(named("BASE_URL")) // provided in app module
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(
                get<Json>().asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .client(get())
            .build()
            .create(CardsApiService::class.java)
    }
}