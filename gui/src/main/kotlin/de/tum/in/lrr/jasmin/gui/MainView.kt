package de.tum.`in`.lrr.jasmin.gui

import javafx.scene.control.Button
import javafx.scene.layout.BorderPane
import org.fxmisc.flowless.VirtualizedScrollPane
import org.fxmisc.richtext.CodeArea
import org.fxmisc.richtext.LineNumberFactory
import tornadofx.View
import tornadofx.action

class MainView : View() {
    override val root: BorderPane by fxml("/views/main_view.fxml")
    private val buttonRun: Button by fxid()
    private val codeArea = CodeArea()

    init {
        title = "Jasmin"
        primaryStage.isMaximized = true

        codeArea.paragraphGraphicFactory = LineNumberFactory.get(codeArea)
        root.center = VirtualizedScrollPane(codeArea)

        buttonRun.action {
            println("Run:\n" + codeArea.text)
        }
    }
}