package org.openjfx.ui


import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage


open class StageWrapper : Application() {
    private var homePage: HomePage? = null

    override fun start(stage: Stage) {
        homePage = HomePage(stage, 1200.0, 900.0)

        stage.title = "JavaFX Realtime Chart Demo"
        stage.scene = Scene(homePage?.root, 1200.0, 900.0)
        stage.show()
    }

    override fun stop() {
        homePage?.close()
    }
}