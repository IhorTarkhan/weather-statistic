package org.openjfx.dto

data class WeatherResponseDto(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val currently: WeatherDataDto,
    val hourly: WeatherDataListDto
)