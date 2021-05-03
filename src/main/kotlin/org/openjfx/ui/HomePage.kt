package org.openjfx.ui


import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.input.KeyEvent
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import org.openjfx.util.UtilGson.Companion.gsonInstance
import java.io.File


class HomePage(stage: Stage, width: Double, height: Double) {
    val root = createGrid()
    var errorMessageRow = 4
    private var graphPage: GraphPage? = null
    private val jsonString: String = File("./src/main/resources/city.list.json").readText(Charsets.UTF_8)
    private val cities =
        gsonInstance.fromJson<List<Map<String, Any>>>(jsonString, List::class.java)


    init {
        val comboBox = createComboBox(cities.map { it["name"].toString() ?: "NaN" })
        val dayBefore = createTextField("Enter count day before today", 1)
        val dayAfter = createTextField("Enter count day after today", 2)

        val submit = createButton("Submit", 0)
        val clear = createButton("Clear", 1)

        submit.onMouseClicked = EventHandler {
            try {
                val dayBeforeValue = dayBefore.text.toLong()
                val dayAfterValue = dayAfter.text.toLong()

                val coordinates = cities.first { it["name"] == comboBox.editor.text }["coord"] as Map<String, Double>
                val lonValue = coordinates["lon"]!!
                val latValue = coordinates["lat"]!!

                if (dayAfterValue > 10 || dayBeforeValue > 10) {
                    informUser("Not recommend set day value more then 10 (will load to long)")
                    return@EventHandler
                }

                graphPage = GraphPage(dayBeforeValue, dayAfterValue, lonValue, latValue)
                stage.scene = Scene(graphPage?.root, width, height)
            } catch (e: Exception) {
                informUser("Invalid values entered")
            }
        }
        clear.onMouseClicked = EventHandler {
            comboBox.editor.text = ""
            dayBefore.text = ""
            dayAfter.text = ""
        }
    }

    private fun informUser(text: String) {
        val label = Label(text)
        GridPane.setConstraints(label, 4, errorMessageRow++)
        root.children.add(label)
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

    private fun createComboBox(data: List<String>, gridRow: Int = 0, gridColumn: Int = 0): ComboBox<String> {
        val comboBox = ComboBox(FXCollections.observableArrayList(data))
        comboBox.isEditable = true

        comboBox.onMouseMoved = EventHandler {
            comboBox.show()
        }
        comboBox.addEventHandler(KeyEvent.KEY_RELEASED) {
            comboBox.items = data
                .filter { it.toLowerCase().contains(comboBox.editor.text.toLowerCase()) }
                .take(20)
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
