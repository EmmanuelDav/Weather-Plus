package com.emmanueliyke.weatherplus.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.emmanueliyke.weatherplus.data.local.dao.CityWeatherDao
import com.emmanueliyke.weatherplus.data.local.database.WeatherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
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