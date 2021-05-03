package org.openjfx


import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage


open class UI : Application() {
    private var homePage: HomePage? = null

    override fun start(stage: Stage) {
        homePage = HomePage(stage, 1500.0, 600.0)

        stage.title = "JavaFX Realtime Chart Demo"
        stage.scene = Scene(homePage?.root, 1500.0, 600.0)
        stage.show()
    }

    override fun stop() {
        homePage?.close()
    }
}