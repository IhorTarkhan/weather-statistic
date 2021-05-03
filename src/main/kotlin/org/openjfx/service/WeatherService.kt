package org.openjfx.service

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
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

    fun getWeatherAsync(
        latitude: Double,
        longitude: Double,
        dateTime: LocalDateTime,
        onResponse: (WeatherResponseDto) -> Unit
    ) {
        val request = getRequest(latitude, longitude, dateTime)
        UtilHttpClient.httpClientInstance.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) =
                e.printStackTrace()

            override fun onResponse(call: Call, response: Response) {
                onResponse(getDto(response))
            }
        })
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
            responseDto = getDto(response)
        }
        return responseDto
    }

    private fun getDto(response: Response): WeatherResponseDto {
        if (!response.isSuccessful) throw IOException("Unexpected code $response")
        return UtilGson.gsonInstance.fromJson(response.body()?.string() ?: "{}", WeatherResponseDto::class.java)
    }
}