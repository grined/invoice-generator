package com.grined.toptal.invoice

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

class App : Application() {
    private val layout = "manual.fxml"

    override fun start(primaryStage: Stage?) {
        primaryStage?.scene = Scene(FXMLLoader.load<Parent?>(javaClass.classLoader.getResource(layout)))
        primaryStage?.isResizable = false
        primaryStage?.title = "Toptal Invoice Generator"
        primaryStage?.show()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(App::class.java)
        }
    }
}
