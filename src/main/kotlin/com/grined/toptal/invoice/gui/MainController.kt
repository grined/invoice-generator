package com.grined.toptal.invoice.gui

import com.grined.toptal.invoice.generator.DocGenerator
import com.grined.toptal.invoice.generator.PdfGenerator
import com.grined.toptal.invoice.toptal.ResponseParser
import com.grined.toptal.invoice.toptal.ToptalAccessor
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.DatePicker
import javafx.scene.control.Label
import javafx.scene.control.TextField
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

    fun initialize() {
        print("Controller working")
        datePicker.value = LocalDate.now();
        generateButton.setOnAction {
            generate()
            print(urlField.text + " " + datePicker.value)
        }
    }

    fun generate() {
        updateStatus("Accessing toptal . . .")
        val toptalAccessor = ToptalAccessor()
        val text = toptalAccessor.getInvoice(urlField.text)
        updateStatus("Success. Parsing . . .")
        val invoiceInfo = ResponseParser.extractInvoiceInfo(
                rawHtml = text,
                useInvoiceDate = false,
                manualDateDeadline = datePicker.value)
        updateStatus("Success. Generating docx . . .")
        val generatedDoc = DocGenerator.generateDoc(invoiceInfo)
        updateStatus("Success. Generating pdf . . .")
        PdfGenerator.buildPdf(generatedDoc)
        updateStatus("Generated successfull!")
    }

    fun updateStatus(status : String){
        statusLabel.text = status;
    }
}


