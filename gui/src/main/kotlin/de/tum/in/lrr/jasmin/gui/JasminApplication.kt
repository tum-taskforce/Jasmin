package de.tum.`in`.lrr.jasmin.gui

import tornadofx.App
import tornadofx.launch

class JasminApplication : App(MainView::class)

fun main(args: Array<String>) = launch<JasminApplication>(args)