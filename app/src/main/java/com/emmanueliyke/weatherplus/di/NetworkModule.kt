package com.emmanueliyke.weatherplus.di

import com.emmanueliyke.weatherplus.BuildConfig
import com.emmanueliyke.weatherplus.data.remote.api.WeatherApiService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import java.util.concurrent.TimeUnit
import kotlin.jvm.java


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkhttpClient(): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder().baseUrl("https://api.openweathermap.org/data/2.5/").client(okHttpClient).addConverterFactory(
            GsonConverterFactory.create()
        ).build()


    @Provides
    @Singleton
    fun provideWeatherApiService(retrofit: Retrofit): WeatherApiService =
        retrofit.create(WeatherApiService::class.java)
}