package com.emmanueliyke.weatherplus.util

import com.emmanueliyke.weatherplus.domain.model.CityWeather
import com.emmanueliyke.weatherplus.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeWeatherRepository : WeatherRepository {

    private val citiesFlow = MutableStateFlow<List<CityWeather>>(emptyList())

    var shouldFailRefresh = false
    var toggleFavoriteCalled = false
    var lastToggledCityId: Int? = null

    fun setCities(cities: List<CityWeather>) {
        citiesFlow.value = cities
    }

    override fun observeCities(): Flow<List<CityWeather>> = citiesFlow

    override suspend fun refreshCities(): Result<Unit> {
        return if (shouldFailRefresh) {
            Result.failure(Exception("No internet"))
        } else {
            Result.success(Unit)
        }
    }

    override suspend fun getCityById(cityId: Int): CityWeather? {
        return citiesFlow.value.find { it.cityId == cityId }
    }

    override suspend fun toggleFavorite(cityId: Int, isFavorite: Boolean) {
        toggleFavoriteCalled = true
        lastToggledCityId = cityId
        citiesFlow.update { cities ->
            cities.map { if (it.cityId == cityId) it.copy(isFavorite = isFavorite) else it }
        }
    }
}