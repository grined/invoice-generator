package com.grined.toptal.invoice.gui

import com.grined.toptal.invoice.generator.DocGenerator
import com.grined.toptal.invoice.generator.PdfGenerator
import com.grined.toptal.invoice.toptal.ResponseParser
import com.grined.toptal.invoice.toptal.ToptalAccessor
import javafx.fxml.FXML
import javafx.scene.control.*
import java.time.LocalDate

class MainController {
    @FXML
    lateinit var generateButton: Button
    @FXML
    lateinit var urlField: TextField
    @FXML
    lateinit var datePicker: DatePicker
    @FXML
    lateinit var statusLabel: Label
    @FXML
    lateinit var rbInvoiceDate: RadioButton
    @FXML
    lateinit var rbCustomDate: RadioButton

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
    }

    fun actionOnRbSelected() {
        datePicker.isDisable = toggleGroup.selectedToggleProperty().get() == rbInvoiceDate
    }


    fun generate() {
        updateStatus("Accessing toptal . . .")
        val toptalAccessor = ToptalAccessor()
        val text = toptalAccessor.getInvoice(urlField.text)
        updateStatus("Success. Parsing . . .")
        val invoiceInfo = ResponseParser.extractInvoiceInfo(
//                rawHtml = FileReader("test.data").readText(),
                rawHtml = text,
                useInvoiceDate = datePicker.isDisable,
                manualDateDeadline = datePicker.value)
        updateStatus("Success. Generating docx . . .")
        val generatedDoc = DocGenerator.generateDoc(invoiceInfo)
        updateStatus("Success. Generating pdf . . .")
        PdfGenerator.buildPdf(generatedDoc)
        updateStatus("Generated successfull!")
    }

    fun updateStatus(status : String){
        statusLabel.text = "Status: " + status
    }
}


