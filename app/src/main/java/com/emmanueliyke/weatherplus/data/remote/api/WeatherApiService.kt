package com.emmanueliyke.weatherplus.data.remote.api

import com.emmanueliyke.weatherplus.data.remote.dto.CityGroupResponseDto
import com.emmanueliyke.weatherplus.data.remote.dto.WeatherResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    // Fetch multiple cities in one call — used on home screen load/refresh
    @GET("group")
    suspend fun getCitiesWeather(
        @Query("id") ids: String,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String
    ): CityGroupResponseDto

    // Fetch single city — used for detail screen refresh
    @GET("weather")
    suspend fun getCityWeather(
        @Query("id") cityId: Int,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String
    ): WeatherResponseDto
}