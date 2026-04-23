package com.emmanueliyke.weatherplus.data.repository

import com.emmanueliyke.weatherplus.data.local.dao.CityWeatherDao
import com.emmanueliyke.weatherplus.data.remote.api.CityConstants
import com.emmanueliyke.weatherplus.data.remote.api.WeatherApiService
import com.emmanueliyke.weatherplus.data.remote.mapper.toDomain
import com.emmanueliyke.weatherplus.data.remote.mapper.toEntity
import com.emmanueliyke.weatherplus.domain.model.CityWeather
import com.emmanueliyke.weatherplus.domain.repository.WeatherRepository
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
        val response = api.getCitiesWeather(
            ids = CityConstants.CITY_IDS_PARAM,
            apiKey = CityConstants.API_KEY
        )

        val currentFavorites = dao.observeAll()
            .map { it.filter { entity -> entity.isFavorite }.map { entity -> entity.cityId } }
            .let { flow ->
                var result = emptyList<Int>()
                flow.collect { result = it }
                result
            }

        val entities = response.list.map { dto ->
            dto.toEntity(isFavorite = dto.id in currentFavorites)
        }

        dao.upsertAll(entities)
    }

    override suspend fun getCityById(cityId: Int): CityWeather? =
        dao.getCityById(cityId)?.toDomain()

    override suspend fun toggleFavorite(cityId: Int, isFavorite: Boolean) =
        dao.setFavorite(cityId, isFavorite)
}