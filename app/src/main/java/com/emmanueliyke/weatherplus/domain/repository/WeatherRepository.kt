package com.emmanueliyke.weatherplus.domain.repository

import com.emmanueliyke.weatherplus.domain.model.CityWeather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun observeCities(): Flow<List<CityWeather>>

    suspend fun refreshCities(): Result<Unit>

    suspend fun getCityById(cityId: Int): CityWeather?

    suspend fun toggleFavorite(cityId: Int, isFavorite: Boolean)
}