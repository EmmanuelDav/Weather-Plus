package com.emmanueliyke.weatherplus.domain.model


data class CityWeather(
    val cityId: Int,
    val name: String,
    val country: String,
    val tempCelsius: Double,
    val feelsLike: Double,
    val humidity: Int,
    val description: String,
    val iconCode: String,
    val windSpeed: Double,
    val visibility: Int,
    val pressure: Int,
    val isFavorite: Boolean,
    val lastUpdated: Long
) {
    val iconUrl: String
        get() = "https://openweathermap.org/img/wn/$iconCode@2x.png"

    val tempDisplay: String
        get() = "${tempCelsius.toInt()}°C"

    val feelsLikeDisplay: String
        get() = "${feelsLike.toInt()}°C"

    val windDisplay: String
        get() = "${windSpeed} m/s"

    val humidityDisplay: String
        get() = "$humidity%"

    val visibilityDisplay: String
        get() = "${visibility / 1000} km"

    val pressureDisplay: String
        get() = "$pressure hPa"
}