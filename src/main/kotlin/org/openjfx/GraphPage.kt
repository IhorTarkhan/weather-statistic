package org.openjfx

import javafx.scene.layout.GridPane
import org.openjfx.dto.WeatherDataDto
import org.openjfx.service.WeatherService
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

val weatherService = WeatherService()

class GraphPage {
    val root = GridPane()
    private val scheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    private val temperatureGraphOneThread =
        Graph("Day, Time", "Temperature, C", "Average temperature in hour", "One Thread Charts")
    private val temperatureGraphMultiThread =
        Graph("Day, Time", "Temperature, C", "Average temperature in hour", "Multi Thread Charts")

    init {
        GridPane.setConstraints(temperatureGraphOneThread.instance, 0, 0)
        root.children.add(temperatureGraphOneThread.instance)
        GridPane.setConstraints(temperatureGraphMultiThread.instance, 1, 0)
        root.children.add(temperatureGraphMultiThread.instance)

        val latitude = 37.774929
        val longitude = -122.419418
        val now = LocalDateTime.now()

        val requestsDates = (10 downTo -10)
            .toList()
            .map { now.minusDays(it.toLong()) }
            .toCollection(LinkedList())

        scheduledExecutorService.scheduleAtFixedRate({
            if (requestsDates.isNotEmpty()) {
                val dateTime = requestsDates.poll()
                val response = weatherService.getWeather(latitude, longitude, dateTime)
                temperatureGraphOneThread.addPointAll(response.hourly.data.map(this::transform))
            }
        }, 0, 1, TimeUnit.MILLISECONDS)

        requestsDates.parallelStream().forEach {
            weatherService.getWeatherAsync(latitude, longitude, it) { response ->
                temperatureGraphMultiThread.addPointAll(response.hourly.data.map(this::transform))
            }
        }
    }

    private fun transform(newData: WeatherDataDto) =
        Pair(
            newData.time.toString(),
            newData.temperature.toInt()
        )

    fun close() {
        scheduledExecutorService.shutdown()
    }
}
