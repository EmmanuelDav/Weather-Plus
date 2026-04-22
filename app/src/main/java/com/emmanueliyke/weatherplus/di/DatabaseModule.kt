package com.emmanueliyke.weatherplus.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@Module
object DatabaseModule {


    @Provides
    @Singleton
    fun provideWeatherDatabaseModule(@ApplicationContext context: Context): WeatherDatabase =
        Room.databaseBuilder(context, WeatherDatabase::class.java, "weather_database").build()


    @Provides
    @Singleton
    fun provideCityWeatherDao(database: WeatherDatabase): CityWeatherDao =
        database.cityWeatherDao()
}