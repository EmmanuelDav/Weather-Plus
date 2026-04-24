package com.emmanueliyke.weatherplus.util


import com.emmanueliyke.weatherplus.domain.model.CityWeather

fun fakeCityWeather(
    cityId: Int = 1,
    name: String = "Lagos",
    country: String = "NG",
    tempCelsius: Double = 30.0,
    isFavorite: Boolean = false
) = CityWeather(
    cityId = cityId,
    name = name,
    country = country,
    tempCelsius = tempCelsius,
    feelsLike = 28.0,
    humidity = 80,
    description = "Sunny",
    iconCode = "01d",
    windSpeed = 5.0,
    visibility = 10000,
    pressure = 1012,
    isFavorite = isFavorite,
    lastUpdated = System.currentTimeMillis()
)