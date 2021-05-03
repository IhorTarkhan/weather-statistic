package org.openjfx.dto

data class WeatherDataDto(
    val time: Long,
    val temperature: Double,
    val pressure: Double,
)