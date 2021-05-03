package org.openjfx.service

import okhttp3.Request
import org.openjfx.dto.WeatherResponseDto
import org.openjfx.property.DarkSkyProperty
import org.openjfx.util.UtilGson
import org.openjfx.util.UtilHttpClient
import java.io.IOException
import java.time.LocalDateTime

class WeatherService {
    fun getWeather(latitude: Double, longitude: Double, dateTime: LocalDateTime): WeatherResponseDto {
        val request = getRequest(latitude, longitude, dateTime)
        return call(request)
    }

    private fun getRequest(latitude: Double, longitude: Double, dateTime: LocalDateTime): Request {
        val dateTimeFormatted = dateTime.toString().substringBeforeLast('.')
        return Request.Builder()
            .url("https://dark-sky.p.rapidapi.com/$latitude,$longitude,$dateTimeFormatted?units=si")
            .get()
            .addHeader("x-rapidapi-key", DarkSkyProperty.getKey())
            .addHeader("x-rapidapi-host", "dark-sky.p.rapidapi.com")
            .build()
    }

    private fun call(request: Request): WeatherResponseDto {
        val responseDto: WeatherResponseDto
        UtilHttpClient.httpClientInstance.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            responseDto =
                UtilGson.gsonInstance.fromJson(response.body()?.string() ?: "{}", WeatherResponseDto::class.java)
        }
        return responseDto
    }
}