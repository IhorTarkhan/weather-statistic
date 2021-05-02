package org.openjfx

import javafx.scene.layout.GridPane
import java.time.LocalTime
import java.util.concurrent.Executors
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit

class GraphPage {
    val root = GridPane()
    private val scheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    private val graph = Graph("Time/s", "Value", "Data Series", "Realtime JavaFX Charts")

    init {
        GridPane.setConstraints(graph.instance, 0, 0)
        root.children.add(graph.instance)
        scheduledExecutorService.scheduleAtFixedRate({
            graph.addPoint(LocalTime.now().toString(), ThreadLocalRandom.current().nextInt(100))
        }, 0, 1, TimeUnit.SECONDS)
    }

    fun close() {
        scheduledExecutorService.shutdown()
    }
}
