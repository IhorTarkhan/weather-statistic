package org.openjfx

import javafx.scene.layout.GridPane
import org.openjfx.dto.WeatherDataDto
import org.openjfx.service.WeatherService
import java.time.Instant.ofEpochSecond
import java.time.LocalDateTime
import java.time.LocalDateTime.ofInstant
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class GraphPage {
    val root = GridPane()
    private val xTitle = "Day, Hour"
    private val weatherService = WeatherService()
    private val scheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    private val temperatureGraphOneThread =
        Graph(xTitle, "Temperature, C", "Average temperature in hour", "Temperature(One Thread Charts)")
    private val temperatureGraphMultiThread =
        Graph(xTitle, "Temperature, C", "Average temperature in hour", "Temperature(Multi Thread Charts)")

    private val pressureGraphOneThread =
        Graph(xTitle, "Pressure, Pa", "Average pressure in hour", "Pressure(One Thread Charts)", false)
    private val pressureGraphMultiThread =
        Graph(xTitle, "Pressure, Pa", "Average pressure in hour", "Pressure(Multi Thread Charts)", false)

    init {
        GridPane.setConstraints(temperatureGraphOneThread.instance, 0, 0)
        root.children.add(temperatureGraphOneThread.instance)


        GridPane.setConstraints(temperatureGraphMultiThread.instance, 1, 0)
        root.children.add(temperatureGraphMultiThread.instance)

        GridPane.setConstraints(pressureGraphOneThread.instance, 0, 1)
        root.children.add(pressureGraphOneThread.instance)
        GridPane.setConstraints(pressureGraphMultiThread.instance, 1, 1)
        root.children.add(pressureGraphMultiThread.instance)

        val latitude = 37.774929
        val longitude = -122.419418
        val now = LocalDateTime.now()

        val requestsDates = (10 downTo -10)
            .toList()
            .map { now.minusDays(it.toLong()) }
            .toCollection(LinkedList())

        Thread {
            val linkedListForOneThreadTemperature = LinkedList(requestsDates)
            scheduledExecutorService.scheduleAtFixedRate({
                if (linkedListForOneThreadTemperature.isNotEmpty()) {
                    val dateTime = linkedListForOneThreadTemperature.poll()
                    val response = weatherService.getWeather(latitude, longitude, dateTime)
                    temperatureGraphOneThread.addPointAll(response.map(this::transformTemperature))
                }
            }, 0, 100, TimeUnit.MILLISECONDS)
        }.run()

        Thread {
            val linkedListForOneThreadPressure = LinkedList(requestsDates)
            scheduledExecutorService.scheduleAtFixedRate({
                if (linkedListForOneThreadPressure.isNotEmpty()) {
                    val dateTime = linkedListForOneThreadPressure.poll()
                    val response = weatherService.getWeather(latitude, longitude, dateTime)
                    pressureGraphOneThread.addPointAll(response.map(this::transformPressure))
                }
            }, 0, 100, TimeUnit.MILLISECONDS)
        }.run()

        Thread {
            requestsDates.parallelStream().forEach {
                weatherService.getWeatherAsync(latitude, longitude, it) { response ->
                    temperatureGraphMultiThread.addPointAll(response.map(this::transformTemperature))
                }
            }
        }.run()

        Thread {
            requestsDates.parallelStream().forEach {
                weatherService.getWeatherAsync(latitude, longitude, it) { response ->
                    pressureGraphMultiThread.addPointAll(response.map(this::transformPressure))
                }
            }
        }.run()
    }

    private fun transformTemperature(newData: WeatherDataDto) =
        Pair(
            ofInstant(
                ofEpochSecond(newData.time),
                TimeZone.getTimeZone(newData.timezone).toZoneId()
            ), newData.temperature
        )

    private fun transformPressure(newData: WeatherDataDto) =
        Pair(
            ofInstant(
                ofEpochSecond(newData.time),
                TimeZone.getTimeZone(newData.timezone).toZoneId()
            ), newData.pressure
        )

    fun close() {
        scheduledExecutorService.shutdown()
    }
}
