package com.emmanueliyke.weatherplus.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.emmanueliyke.weatherplus.data.local.entries.CityWeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityWeatherDao {

    @Query("SELECT * FROM cities ORDER BY isFavorite DESC, name ASC")
    fun observeAll(): Flow<List<CityWeatherEntity>>

    @Query("SELECT * FROM cities WHERE cityId = :id")
    suspend fun getCityById(id: Int): CityWeatherEntity?

    @Query("SELECT * FROM cities WHERE isFavorite = 1 ORDER BY name ASC LIMIT 1")
    suspend fun getTopFavorite(): CityWeatherEntity?

    @Upsert
    suspend fun upsertAll(cities: List<CityWeatherEntity>)

    @Query("UPDATE cities SET isFavorite = :isFavorite WHERE cityId = :cityId")
    suspend fun setFavorite(cityId: Int, isFavorite: Boolean)
}