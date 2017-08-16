package com.grined.toptal.invoice.gui

import com.grined.toptal.invoice.generator.DocGenerator
import com.grined.toptal.invoice.generator.InvoiceConstructor
import com.grined.toptal.invoice.generator.PdfGenerator
import com.grined.toptal.invoice.toptal.ResponseParser
import com.grined.toptal.invoice.toptal.ToptalAccessor
import javafx.application.Platform
import javafx.scene.control.*
import java.time.LocalDate
import java.util.concurrent.CompletableFuture


class ToptalFormController {
//    @FXML
    lateinit var generateButton: Button
//    @FXML
    lateinit var urlField: TextField
//    @FXML
    lateinit var datePicker: DatePicker
//    @FXML
    lateinit var statusLabel: Label
//    @FXML
    lateinit var rbInvoiceDate: RadioButton
//    @FXML
    lateinit var rbCustomDate: RadioButton
//    @FXML
    lateinit var cbUseCustomAmount: CheckBox
//    @FXML
    lateinit var amountField: TextField

    val toggleGroup = ToggleGroup()

    fun initialize() {
        rbCustomDate.toggleGroup = toggleGroup
        rbInvoiceDate.toggleGroup = toggleGroup
        datePicker.value = LocalDate.now()
        setupActions()
    }

    private fun setupActions() {
        generateButton.setOnAction {
            generate()
            print(urlField.text + " " + datePicker.value)
        }
        rbCustomDate.setOnAction { actionOnRbSelected() }
        rbInvoiceDate.setOnAction { actionOnRbSelected() }
        cbUseCustomAmount.setOnAction { actionOnCbChanged() }
    }

    private fun actionOnCbChanged() {
        if (!cbUseCustomAmount.isSelected) {
            amountField.text = ""
        }
        amountField.isDisable = !cbUseCustomAmount.isSelected
    }

    private fun actionOnRbSelected() {
        datePicker.isDisable = toggleGroup.selectedToggleProperty().get() == rbInvoiceDate
    }


    private fun generate() {
        CompletableFuture.supplyAsync {
            updateStatus("Accessing toptal . . .")
             val toptalAccessor = ToptalAccessor()
//            val toptalAccessor = ToptalAccessorLocal()
            toptalAccessor.getInvoice(urlField.text)
        }.thenApply{ text ->
            updateStatus("Success. Parsing . . .")
            ResponseParser.parse(text)
        }.thenApply { invoiceInfo ->
            updateStatus("Success. Constructing . . .")
            InvoiceConstructor.construct(
                    invoiceInfo = invoiceInfo,
                    useInvoiceDate = datePicker.isDisable,
                    manualDateDeadline = datePicker.value,
                    useCustomAmount = cbUseCustomAmount.isSelected,
                    customAmount = amountField.text)
        }.thenApply { invoiceInfo ->
            updateStatus("Success. Generating docx . . .")
            DocGenerator.generateDoc(invoiceInfo)
        }.thenApply { generatedDoc ->
            updateStatus("Success. Generating pdf . . .")
            PdfGenerator.buildPdf(generatedDoc)
        }.thenAccept { updateStatus("Generated successfull!") }
    }

    private fun updateStatus(status: String) {
        Platform.runLater { statusLabel.text = "Status: " + status }
    }
}


