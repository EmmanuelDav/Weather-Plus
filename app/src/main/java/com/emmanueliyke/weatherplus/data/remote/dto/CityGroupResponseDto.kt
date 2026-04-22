package com.emmanueliyke.weatherplus.data.remote.dto


import com.google.gson.annotations.SerializedName

data class CityGroupResponseDto(
    @SerializedName("list") val list: List<WeatherResponseDto>
)