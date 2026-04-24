package com.emmanueliyke.weatherplus.data.remote.api

import com.emmanueliyke.weatherplus.data.remote.dto.WeatherResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("weather")
    suspend fun getCityWeather(
        @Query("id") cityId: Int,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String
    ): WeatherResponseDto
}