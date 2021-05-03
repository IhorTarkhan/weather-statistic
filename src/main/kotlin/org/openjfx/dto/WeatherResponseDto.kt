package org.openjfx.dto

data class WeatherResponseDto(
    val timezone: String,
    val hourly: WeatherDataListDto
)