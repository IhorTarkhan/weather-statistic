package org.openjfx.dto

data class WeatherDataDto(
    val time: Long,
    var timezone: String,
    val temperature: Double,
    val pressure: Double,
)