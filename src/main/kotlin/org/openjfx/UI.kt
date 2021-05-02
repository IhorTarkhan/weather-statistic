package org.openjfx


import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import java.time.LocalTime.now
import java.util.concurrent.Executors
import java.util.concurrent.ThreadLocalRandom.current
import java.util.concurrent.TimeUnit


open class UI : Application() {
    private val scheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    override fun start(stage: Stage) {
        val xTitle = "Time/s"
        val yTitle = "Value"
        val seriesTitle = "Data Series"
        val chartsTitle = "Realtime JavaFX Charts"

        val graph = Graph(xTitle, yTitle, seriesTitle, chartsTitle)

        scheduledExecutorService.scheduleAtFixedRate({
            graph.addPoint(now().toString(), current().nextInt(100))
        }, 0, 1, TimeUnit.SECONDS)

        stage.title = "JavaFX Realtime Chart Demo"
        stage.scene = Scene(graph.lineChart, 800.0, 600.0)
        stage.show()
    }

    override fun stop() {
        scheduledExecutorService.shutdownNow()
    }
}