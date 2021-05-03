package org.openjfx.dto

data class WeatherDataDto(
    val time: Long,
    val summary: String,
    val timezone: String,
    val temperature: Double,
    val temperatureError: Double,
    val precipProbability: Double,
    val precipProbabilityError: Double,
    val precipType: String
)