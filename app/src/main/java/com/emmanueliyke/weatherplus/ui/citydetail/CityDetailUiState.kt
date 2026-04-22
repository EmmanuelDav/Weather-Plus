package com.emmanueliyke.weatherplus.ui.citydetail

import com.emmanueliyke.weatherplus.domain.model.CityWeather

data class CityDetailUiState(
    val city: CityWeather? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

