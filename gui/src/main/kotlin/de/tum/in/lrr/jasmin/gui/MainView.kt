package de.tum.`in`.lrr.jasmin.gui

import de.tum.`in`.lrr.jasmin.core.DataSpace
import de.tum.`in`.lrr.jasmin.core.Jasmin
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import org.fxmisc.richtext.CodeArea
import org.fxmisc.richtext.LineNumberFactory
import org.fxmisc.richtext.model.StyleSpans
import org.fxmisc.richtext.model.StyleSpansBuilder
import tornadofx.View
import tornadofx.action
import java.util.regex.Pattern

class MainView : View() {
    override val root: BorderPane by fxml("/views/main_view.fxml")
    private val buttonRun: Button by fxid()
    private val codeArea: CodeArea by fxid()

    private val inputRegA: TextField by fxid()
    private val inputRegB: TextField by fxid()
    private val inputRegC: TextField by fxid()
    private val inputRegD: TextField by fxid()
    private val registerInputs = mapOf(
            "EAX" to inputRegA,
            "EBX" to inputRegB,
            "ECX" to inputRegC,
            "EDX" to inputRegD
    )

    private val checkZero: CheckBox by fxid()
    private val checkSign: CheckBox by fxid()
    private val checkCarry: CheckBox by fxid()
    private val checkOverflow: CheckBox by fxid()

    private val jasmin = Jasmin()

    private val pattern by lazy {
        val mnemos = "\\b(" + jasmin.mnemos.joinToString("|") + ")\\b"
        val registers = "\\b(" + DataSpace.getRegisterList().joinToString("|") + ")\\b"
        val sizes = "\\b(BYTE|WORD|DWORD|QWORD)\\b"
        val numbers = "\\b(-?\\d+|0x[0-9a-f]+)\\b"
        val comments = "(;.+)$"
        val labels = "^(\\w+:)"
        Pattern.compile("""
            (?<MNEMO>$mnemos)|
            (?<REGISTER>$registers)|
            (?<SIZE>$sizes)|
            (?<NUMBER>$numbers)|
            (?<COMMENT>$comments)|
            (?<LABEL>$labels)
        """.trimIndent().replace("\n", ""), Pattern.CASE_INSENSITIVE or Pattern.MULTILINE)
    }

    init {
        title = "Jasmin"

        codeArea.paragraphGraphicFactory = LineNumberFactory.get(codeArea)
        codeArea.richChanges()
                .filter { it.inserted != it.removed }
                .subscribe {
                    codeArea.setStyleSpans(0, computeHighlighting(codeArea.text))
                }

        buttonRun.action {
            jasmin.reset()
            println()
            println("Run:\n" + codeArea.text)
            val code = codeArea.text.lines().toTypedArray()
            jasmin.parse(code)
            jasmin.execute(code)
            printRegisters()
            updateRegistersAndFlags()
        }

        registerInputs.forEach { name, input ->
            input.textProperty().addListener { _, _, newValue ->
                val value = newValue.toLongOrNull() ?: return@addListener
                val address = jasmin.data.getRegisterArgument(name)
                jasmin.data.put(value, address, null)
            }
        }

        checkZero.selectedProperty().addListener { _, _, newValue -> jasmin.data.fZero = newValue }
        checkSign.selectedProperty().addListener { _, _, newValue -> jasmin.data.fSign = newValue }
        checkCarry.selectedProperty().addListener { _, _, newValue -> jasmin.data.fCarry = newValue }
        checkOverflow.selectedProperty().addListener { _, _, newValue -> jasmin.data.fOverflow = newValue }
    }

    private fun updateRegistersAndFlags() {
        registerInputs.forEach { name, input ->
            input.text = jasmin.data.getRegisterArgument(name).getShortcut().toString()
        }
        checkZero.isSelected = jasmin.data.fZero
        checkSign.isSelected = jasmin.data.fSign
        checkCarry.isSelected = jasmin.data.fCarry
        checkOverflow.isSelected = jasmin.data.fOverflow
    }

    private fun computeHighlighting(text: String): StyleSpans<Collection<String>> {
        val matcher = pattern.matcher(text)
        var last = 0
        return buildStyleSpans {
            while (matcher.find()) {
                val styleClass = when {
                    matcher.group("MNEMO") != null -> "asm-mnemo"
                    matcher.group("REGISTER") != null -> "asm-register"
                    matcher.group("SIZE") != null -> "asm-size"
                    matcher.group("NUMBER") != null -> "asm-number"
                    matcher.group("COMMENT") != null -> "asm-comment"
                    matcher.group("LABEL") != null -> "asm-label"
                    else -> "asm-text"
                }
                add(listOf("asm-text"), matcher.start() - last)
                add(listOf(styleClass), matcher.end() - matcher.start())
                last = matcher.end()
            }
            add(listOf("asm-text"), text.length - last)
        }
    }

    private fun <S> buildStyleSpans(f: StyleSpansBuilder<S>.() -> Unit): StyleSpans<S> {
        val spansBuilder = StyleSpansBuilder<S>()
        spansBuilder.f()
        return spansBuilder.create()
    }

    private fun printRegisters() {
        println("=== Registers ===")
        for (reg in jasmin.data.registerSets) {
            println(reg.E + ": " + reg.aE.getShortcut())
        }
    }
}