package com.grined.toptal.invoice.generator

import com.grined.toptal.invoice.DBAccessor
import com.grined.toptal.invoice.properties.PropertyHolder
import javafx.application.Platform
import javafx.scene.control.Label
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.CompletableFuture

object RadbeeExtraGenerator {
    private val config = PropertyHolder.applicationConfig.radbeeExtra

    fun generateRadbee(date: LocalDate, amount: String, invoiceNumber: String, statusLabel: Label) {
        CompletableFuture.supplyAsync {
            updateStatus("Creating document . . .", statusLabel)
            InvoiceConstructor.construct(
                    moneyAlreadyReceived = false,
                    paidDeadlineDuration = config.paidDurationDays,
                    invoiceDate = date,
                    customAmount = amount,
                    customInvoiceNumber = invoiceNumber
            )
        }.thenApply { invoiceInfo ->
            updateStatus("Success. Generating docx . . .", statusLabel)
            DocGenerator.generateDoc(invoiceInfo, config.template, withSuffix(config.outputDocx, invoiceNumber))
        }.thenApply { generatedDoc ->
            updateStatus("Success. Generating pdf . . .", statusLabel)
            PdfGenerator.buildPdf(generatedDoc, withSuffix(config.outputPdf, invoiceNumber))
        }.thenAccept { updateStatus("Generated successfull!", statusLabel)
        }.thenAccept { DBAccessor.incrementInvoiceNumber(invoiceNumber.toLong()) }
    }

    private fun updateStatus(status: String, statusLabel: Label) {
        Platform.runLater { statusLabel.text = "Status: " + status }
    }

    private fun withSuffix(fileName: String, invoiceNumber: String) : String {
        val position = fileName.lastIndexOf(".")
        return fileName.replaceRange(position, position, "_"+
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"))+ "_" + invoiceNumber)
    }
}