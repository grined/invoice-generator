package com.grined.toptal.invoice.generator

import com.grined.toptal.invoice.properties.PropertyHolder
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.CompletableFuture

object ToptalGenerator {
    private val config = PropertyHolder.applicationConfig.toptal

    fun generateToptal(date: LocalDate, amount: String, invoiceNumber: String, title: String) {
        CompletableFuture.supplyAsync {
            println("Creating document . . .")
            InvoiceConstructor.construct(
                    moneyAlreadyReceived = true,
                    paidDeadlineDuration = config.paidDurationDays,
                    invoiceDate = date,
                    customAmount = amount,
                    customInvoiceNumber = invoiceNumber,
                    customTitle = title)
        }.thenApply { invoiceInfo ->
            println("Success. Generating docx . . .")
            DocGenerator.generateDoc(invoiceInfo, config.template, withSuffix(config.outputDocx))
        }.thenApply { generatedDoc ->
            println("Success. Generating pdf . . .")
            PdfGenerator.buildPdf(generatedDoc, withSuffix(config.outputPdf))
        }.thenAccept { file ->
            println("Generated successfull!")
        }
    }

    private fun withSuffix(fileName: String) : String {
        val position = fileName.lastIndexOf(".")
        return fileName.replaceRange(position, position, "_"+
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd")))
    }
}