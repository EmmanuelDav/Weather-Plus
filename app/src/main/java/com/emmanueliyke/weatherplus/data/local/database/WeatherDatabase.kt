package com.emmanueliyke.weatherplus.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.emmanueliyke.weatherplus.data.local.dao.CityWeatherDao
import com.emmanueliyke.weatherplus.data.local.entries.CityWeatherEntity


@Database(
    entities = [CityWeatherEntity::class],
    version = 1,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun cityWeatherDao(): CityWeatherDao
}