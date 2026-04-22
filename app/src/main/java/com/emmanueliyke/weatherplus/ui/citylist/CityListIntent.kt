package com.emmanueliyke.weatherplus.ui.citylist

sealed interface CityListIntent {
    data class OnSearchQueryChanged(val query: String) : CityListIntent
    data class OnToggleFavorite(val cityId: Int, val isFavorite: Boolean) : CityListIntent
    object OnRefresh : CityListIntent
    object OnClearSearch : CityListIntent
}

