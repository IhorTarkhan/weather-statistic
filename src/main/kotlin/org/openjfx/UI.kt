package org.openjfx

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.StackPane
import javafx.stage.Stage

open class UI : Application() {
    override fun start(stage: Stage) {
        val label = Label("Hello World!")
        stage.scene = Scene(StackPane(label), 640.0, 480.0)
        stage.show()
    }
}