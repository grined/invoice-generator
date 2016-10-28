package com.grined.toptal.invoice.gui

import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.*

fun createGUI() {
    val topPanel = JPanel()
    topPanel.layout = BorderLayout()

    val updateListButton = JButton("Update list")
    val updateLookAndFeelButton = JButton("Update Look&Feel")

    val btnPanel = JPanel()
    btnPanel.layout = BoxLayout(btnPanel, BoxLayout.LINE_AXIS)
    btnPanel.add(updateListButton)
    btnPanel.add(Box.createHorizontalStrut(5))
    btnPanel.add(updateLookAndFeelButton)

    val bottomPanel = JPanel()
    bottomPanel.add(btnPanel)

    val panel = JPanel()
    panel.border = BorderFactory.createEmptyBorder(5, 5, 5, 5)
    panel.layout = BorderLayout()
    panel.add(topPanel, BorderLayout.CENTER)
    panel.add(bottomPanel, BorderLayout.SOUTH)

    val frame = JFrame("Look&Feel Switcher")
    frame.minimumSize = Dimension(300, 200)
    frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
    frame.add(panel)
    frame.pack()
    frame.isVisible = true
}