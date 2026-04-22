package com.emmanueliyke.weatherplus.data.remote.api

import com.emmanueliyke.weatherplus.BuildConfig

object CityConstants {

    val CITY_IDS = listOf(
        2332459,  // Lagos
        2643743,  // London
        292223,   // Dubai
        5128581,  // New York
        1850147,  // Tokyo
        2988507,  // Paris
        2950159,  // Berlin
        6167865,  // Toronto
        2147714,  // Sydney
        1275339,  // Mumbai
        360630,   // Cairo
        184745,   // Nairobi
        1880252,  // Singapore
        3448439,  // São Paulo
        3530597   // Mexico City
    )

    val CITY_IDS_PARAM: String get() = CITY_IDS.joinToString(",")

    val API_KEY: String get() = BuildConfig.WEATHER_API_KEY
}