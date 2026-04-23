package com.emmanueliyke.weatherplus.ui.navigation
import kotlinx.serialization.Serializable

sealed interface NavRoutes {

    @Serializable
    data object CityList : NavRoutes

    @Serializable
    data class CityDetail(val cityId: Int) : NavRoutes
}