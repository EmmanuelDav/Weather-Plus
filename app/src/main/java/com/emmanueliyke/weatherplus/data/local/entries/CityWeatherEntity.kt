package com.emmanueliyke.weatherplus.data.local.entries

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class CityWeatherEntity(
    @PrimaryKey val cityId: Int,
    val name: String,
    val country: String,
    val tempCelsius: Double,
    val feelsLike: Double,
    val humidity: Int,
    val description: String,
    val iconCode: String,
    val windSpeed: Double,
    val visibility: Int,
    val pressure: Int,
    val isFavorite: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
)