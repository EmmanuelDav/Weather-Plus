package com.emmanueliyke.weatherplus.di

import com.emmanueliyke.weatherplus.data.repository.WeatherRepositoryImpl
import com.emmanueliyke.weatherplus.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

class RepositoryModule {

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class RepositoryModule {

        @Binds
        @Singleton
        abstract fun bindWeatherRepository(
            impl: WeatherRepositoryImpl
        ): WeatherRepository
    }


}

