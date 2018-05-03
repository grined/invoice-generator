package com.grined.toptal.invoice.generator

import com.grined.toptal.invoice.properties.PropertyHolder
import javafx.application.Platform
import javafx.scene.control.Label
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.CompletableFuture

object ToptalGenerator {
    private val config = PropertyHolder.applicationConfig.toptal

    fun generateToptal(date: LocalDate, amount: String, invoiceNumber: String, title: String, statusLabel: Label) {
        CompletableFuture.supplyAsync {
            updateStatus("Creating document . . .", statusLabel)
            InvoiceConstructor.construct(
                    moneyAlreadyReceived = true,
                    paidDeadlineDuration = config.paidDurationDays,
                    invoiceDate = date,
                    customAmount = amount,
                    customInvoiceNumber = invoiceNumber,
                    customTitle = title)
        }.thenApply { invoiceInfo ->
            updateStatus("Success. Generating docx . . .", statusLabel)
            DocGenerator.generateDoc(invoiceInfo, config.template, withSuffix(config.outputDocx))
        }.thenApply { generatedDoc ->
            updateStatus("Success. Generating pdf . . .", statusLabel)
            PdfGenerator.buildPdf(generatedDoc, withSuffix(config.outputPdf))
        }.thenAccept { updateStatus("Generated successfull!", statusLabel) }
    }

    private fun updateStatus(status: String, statusLabel: Label) {
        Platform.runLater { statusLabel.text = "Status: " + status }
    }

    private fun withSuffix(fileName: String) : String {
        val position = fileName.lastIndexOf(".")
        return fileName.replaceRange(position, position, "_"+
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd")))
    }
}