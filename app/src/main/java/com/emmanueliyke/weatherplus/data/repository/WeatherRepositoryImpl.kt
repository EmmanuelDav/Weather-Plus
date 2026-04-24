package com.emmanueliyke.weatherplus.data.repository

import com.emmanueliyke.weatherplus.data.local.dao.CityWeatherDao
import com.emmanueliyke.weatherplus.data.remote.api.CityConstants
import com.emmanueliyke.weatherplus.data.remote.api.WeatherApiService
import com.emmanueliyke.weatherplus.data.remote.mapper.toDomain
import com.emmanueliyke.weatherplus.data.remote.mapper.toEntity
import com.emmanueliyke.weatherplus.domain.model.CityWeather
import com.emmanueliyke.weatherplus.domain.repository.WeatherRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApiService,
    private val dao: CityWeatherDao
) : WeatherRepository {

    override fun observeCities(): Flow<List<CityWeather>> =
        dao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun refreshCities(): Result<Unit> = runCatching {
        val favoriteIds = dao.getFavoriteCityIds().toSet()

        val entities = coroutineScope {
            CityConstants.CITY_IDS.map { cityId ->
                async {
                    try {
                        val dto = api.getCityWeather(
                            cityId = cityId,
                            apiKey = CityConstants.API_KEY
                        )
                        dto.toEntity(isFavorite = dto.id in favoriteIds)
                    } catch (e: Exception) {
                        null
                    }
                }
            }.awaitAll().filterNotNull()
        }

        dao.upsertAll(entities)
    }

    override suspend fun getCityById(cityId: Int): CityWeather? =
        dao.getCityById(cityId)?.toDomain()

    override suspend fun toggleFavorite(cityId: Int, isFavorite: Boolean) =
        dao.setFavorite(cityId, isFavorite)
}