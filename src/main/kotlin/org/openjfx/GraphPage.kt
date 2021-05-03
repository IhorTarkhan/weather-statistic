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
    private val graph = Graph("Time/s", "Value", "Data Series", "Realtime JavaFX Charts")
    private val queue: Queue<WeatherDataDto> = LinkedList()

    init {
        GridPane.setConstraints(graph.instance, 0, 0)
        root.children.add(graph.instance)

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
                weatherService.getWeather(latitude, longitude, dateTime).hourly.data
                    .forEach {
                        graph.addPoint(
                            it.time.toString(),
                            it.temperature.toInt()
                        )
                    }
            }
        }, 0, 1, TimeUnit.MILLISECONDS)
    }

    fun close() {
        scheduledExecutorService.shutdown()
    }
}
