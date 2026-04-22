package com.emmanueliyke.weatherplus.ui.citylist

import com.emmanueliyke.weatherplus.domain.model.CityWeather


data class CityListUiState(
    val cities: List<CityWeather> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val isOffline: Boolean = false,
    val error: String? = null
) {
    val filteredCities: List<CityWeather>
        get() = if (searchQuery.isBlank()) cities
        else cities.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.country.contains(searchQuery, ignoreCase = true)
        }

    val isEmpty: Boolean
        get() = filteredCities.isEmpty() && !isLoading
}