package org.statistic.weather

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.StackPane
import javafx.stage.Stage

open class Test : Application() {
    override fun start(stage: Stage) {
        val javaVersion = SystemInfo.javaVersion()
        val javafxVersion = SystemInfo.javafxVersion()
        val label = Label("Hello, JavaFX $javafxVersion, running on Java $javaVersion.")
        stage.scene = Scene(StackPane(label), 640.0, 480.0)
        stage.show()
    }
}