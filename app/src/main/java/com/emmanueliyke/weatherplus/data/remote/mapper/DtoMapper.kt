package com.emmanueliyke.weatherplus.data.remote.mapper

import com.emmanueliyke.weatherplus.data.local.entries.CityWeatherEntity
import com.emmanueliyke.weatherplus.data.remote.dto.WeatherResponseDto


fun WeatherResponseDto.toEntity(isFavorite: Boolean = false): CityWeatherEntity =
    CityWeatherEntity(
        cityId = id,
        name = name,
        country = sys.country,
        tempCelsius = main.temp,
        feelsLike = main.feelsLike,
        humidity = main.humidity,
        description = weather.firstOrNull()?.description?.replaceFirstChar { it.uppercase() } ?: "",
        iconCode = weather.firstOrNull()?.icon ?: "",
        windSpeed = wind.speed,
        visibility = visibility,
        pressure = main.pressure,
        isFavorite = isFavorite
    )