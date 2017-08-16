package com.grined.toptal.invoice.gui

import com.grined.toptal.invoice.generator.DocGenerator
import com.grined.toptal.invoice.generator.InvoiceConstructor
import com.grined.toptal.invoice.generator.PdfGenerator
import com.grined.toptal.invoice.toptal.InvoiceInfo
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.DatePicker
import javafx.scene.control.Label
import javafx.scene.control.TextField
import java.time.LocalDate
import java.util.concurrent.CompletableFuture


class ManualFormController {
    @FXML
    lateinit var manualGenerateButton: Button
    @FXML
    lateinit var manualDatePicker: DatePicker
    @FXML
    lateinit var manualTitleField: TextField
    @FXML
    lateinit var manualStatusLabel: Label
    @FXML
    lateinit var manualAmountField: TextField
    @FXML
    lateinit var manualInvoiceNumber: TextField

    fun initialize() {
        manualDatePicker.value = LocalDate.now()
        setupActions()
    }

    private fun setupActions() {
        manualGenerateButton.setOnAction {
            generate()
        }
    }

    private fun generate() {
        CompletableFuture.supplyAsync {
            updateStatus("Creating document . . .")
            InvoiceConstructor.construct(
                    invoiceInfo = InvoiceInfo("","","","",""),
                    useInvoiceDate = false,
                    useCustomAmount = true,
                    useCustomInvoiceNumber = true,
                    useCustomTitle = true,
                    manualDateDeadline = manualDatePicker.value,
                    customAmount = manualAmountField.text,
                    customInvoiceNumber = manualInvoiceNumber.text,
                    customTitle = manualTitleField.text)
        }.thenApply { invoiceInfo ->
            updateStatus("Success. Generating docx . . .")
            DocGenerator.generateDoc(invoiceInfo)
        }.thenApply { generatedDoc ->
            updateStatus("Success. Generating pdf . . .")
            PdfGenerator.buildPdf(generatedDoc)
        }.thenAccept { updateStatus("Generated successfull!") }
    }

    private fun updateStatus(status: String) {
        Platform.runLater { manualStatusLabel.text = "Status: " + status }
    }
}


