package com.emmanueliyke.weatherplus.di

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

