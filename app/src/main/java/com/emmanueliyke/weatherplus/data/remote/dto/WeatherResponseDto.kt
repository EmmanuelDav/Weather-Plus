package com.emmanueliyke.weatherplus.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WeatherResponseDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("sys") val sys: SysDto,
    @SerializedName("main") val main: MainDto,
    @SerializedName("weather") val weather: List<WeatherDto>,
    @SerializedName("wind") val wind: WindDto,
    @SerializedName("visibility") val visibility: Int = 0
)

data class SysDto(
    @SerializedName("country") val country: String
)

data class MainDto(
    @SerializedName("temp") val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("pressure") val pressure: Int
)

data class WeatherDto(
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)

data class WindDto(
    @SerializedName("speed") val speed: Double
)