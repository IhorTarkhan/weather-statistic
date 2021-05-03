package org.openjfx


import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.stage.Stage


class HomePage(stage: Stage, width: Double, height: Double) {
    val root = createGrid()
    private var graphPage: GraphPage? = null

    init {
        val name = createTextField(root, "Enter your first name.", 0, 0)
        val lastName = createTextField(root, "Enter your last name.", 0, 1)
        val comment = createTextField(root, "Enter your comment.", 0, 2)

        val submit = createButton(root, "Submit", 1, 0)
        val clear = createButton(root, "Clear", 1, 1)
        submit.onMouseClicked = EventHandler {
            println(name.text)
            println(lastName.text)
            println(comment.text)
            graphPage = GraphPage()
            stage.scene = Scene(graphPage?.root, width, height)
        }
        clear.onMouseClicked = EventHandler {
            name.text = ""
            lastName.text = ""
            comment.text = ""
        }
    }

    private fun createGrid(): GridPane {
        val grid = GridPane()
        grid.padding = Insets(10.0)
        grid.vgap = 5.0
        grid.hgap = 5.0
        return grid
    }

    private fun createTextField(grid: GridPane, title: String, gridColumn: Int, gridRow: Int): TextField {
        val textField = TextField()
        textField.promptText = title
        GridPane.setConstraints(textField, gridColumn, gridRow)
        grid.children.add(textField)
        return textField
    }

    private fun createButton(grid: GridPane, text: String, gridColumn: Int, gridRow: Int): Button {
        val button = Button(text)
        GridPane.setConstraints(button, gridColumn, gridRow)
        grid.children.add(button)
        return button
    }

    fun close() {
        graphPage?.close()
    }
}