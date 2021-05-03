package org.openjfx


import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import javafx.scene.input.KeyEvent
import javafx.scene.layout.GridPane
import javafx.stage.Stage


class HomePage(stage: Stage, width: Double, height: Double) {
    val root = createGrid()
    private var graphPage: GraphPage? = null

    init {
        val name = createTextField("Enter your first name.", 0)
        val lastName = createTextField("Enter your last name.", 1)
        val comment = createTextField("Enter your comment.", 2)

        val submit = createButton("Submit", 0)
        val clear = createButton("Clear", 1)

        val comboBox = createComboBox(listOf("First", "Second", "Third"))
        GridPane.setConstraints(comboBox, 2, 2)
        root.children.add(comboBox)

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

    private fun createTextField(title: String, gridRow: Int, gridColumn: Int = 0): TextField {
        val textField = TextField()
        textField.promptText = title
        GridPane.setConstraints(textField, gridColumn, gridRow)
        root.children.add(textField)
        return textField
    }

    private fun createButton(text: String, gridRow: Int, gridColumn: Int = 1): Button {
        val button = Button(text)
        GridPane.setConstraints(button, gridColumn, gridRow)
        root.children.add(button)
        return button
    }

    private fun createComboBox(data: List<String>, gridRow: Int = 2, gridColumn: Int = 2): ComboBox<String> {
        val comboBox = ComboBox(FXCollections.observableArrayList(data))
        comboBox.isEditable = true

        comboBox.onMouseMoved = EventHandler {
            comboBox.show()
        }
        comboBox.addEventHandler(KeyEvent.KEY_RELEASED) {
            comboBox.items = data
                .filter { it.toLowerCase().contains(comboBox.editor.text.toLowerCase()) }
                .toCollection(FXCollections.observableArrayList())
            comboBox.show()
        }
        comboBox.onMouseMoved = EventHandler {
            if (data.none { comboBox.editor.text == it }) {
                comboBox.editor.text = ""
            }
        }

        GridPane.setConstraints(comboBox, gridRow, gridColumn)
        root.children.add(comboBox)
        return comboBox
    }

    fun close() {
        graphPage?.close()
    }
}