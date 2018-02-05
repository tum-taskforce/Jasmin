package de.tum.`in`.lrr.jasmin.gui

import javafx.scene.control.Button
import javafx.scene.layout.BorderPane
import org.fxmisc.richtext.CodeArea
import org.fxmisc.richtext.LineNumberFactory
import org.fxmisc.richtext.model.StyleSpans
import org.fxmisc.richtext.model.StyleSpansBuilder
import tornadofx.View
import tornadofx.action

class MainView : View() {
    override val root: BorderPane by fxml("/views/main_view.fxml")
    private val buttonRun: Button by fxid()
    private val codeArea: CodeArea by fxid()

    init {
        title = "Jasmin"
        primaryStage.isMaximized = true

        codeArea.paragraphGraphicFactory = LineNumberFactory.get(codeArea)
        codeArea.richChanges()
                .filter { it.inserted != it.removed }
                .subscribe {
                    codeArea.setStyleSpans(0, computeHighlighting(codeArea.text))
                }

        buttonRun.action {
            println("Run:\n" + codeArea.text)
        }
    }

    private fun computeHighlighting(text: String): StyleSpans<Collection<String>> {
        return buildStyleSpans {
            add(emptyList(), text.length)
        }
    }

    private fun <S> buildStyleSpans(f: StyleSpansBuilder<S>.() -> Unit): StyleSpans<S> {
        val spansBuilder = StyleSpansBuilder<S>()
        spansBuilder.f()
        return spansBuilder.create()
    }
}