package de.tum.`in`.lrr.jasmin.gui

import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import tornadofx.View
import tornadofx.bind

class MainView : View() {
    override val root: BorderPane by fxml("/views/main_view.fxml")
    val counterLabel: Label by fxid()
    val counter = SimpleIntegerProperty()

    init {
        counterLabel.bind(counter)
    }

    fun increment() {
        counter.value += 1
    }
}