package com.grined.toptal.invoice.gui

import javafx.application.Platform
import javafx.scene.control.Label
import java.awt.Desktop
import java.io.File

object StatusUpdater {
    fun updateStatus(status: String,
                             statusLabel: Label,
                             completed : Boolean = false,
                             file: File? = null) {
        Platform.runLater {
            statusLabel.text = "Status: $status ${if(completed) "(clickable)" else ""}"
            if (completed) {
                statusLabel.setOnMouseClicked { Desktop.getDesktop().open(file)}
            } else {
                statusLabel.setOnMouseClicked {  }
            }
        }
    }
}