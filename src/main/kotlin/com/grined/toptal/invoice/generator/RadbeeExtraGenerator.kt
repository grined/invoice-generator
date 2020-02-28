package com.grined.toptal.invoice.generator

import com.grined.toptal.invoice.data.DBAccessor
import com.grined.toptal.invoice.properties.PropertyHolder
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.CompletableFuture

object RadbeeExtraGenerator {
    private val config = PropertyHolder.applicationConfig.radbeeExtra

    fun generateRadbee(date: LocalDate, amount: String, invoiceNumber: String) {
        CompletableFuture.supplyAsync {
            println("Creating document . . .")
            InvoiceConstructor.construct(
                    moneyAlreadyReceived = false,
                    paidDeadlineDuration = config.paidDurationDays,
                    invoiceDate = date,
                    customAmount = amount,
                    customInvoiceNumber = invoiceNumber
            )
        }.thenApply { invoiceInfo ->
            println("Success. Generating docx . . .")
            DocGenerator.generateDoc(invoiceInfo, config.template, withSuffix(config.outputDocx, invoiceNumber))
        }.thenApply { generatedDoc ->
            println("Success. Generating pdf . . .")
            PdfGenerator.buildPdf(generatedDoc, withSuffix(config.outputPdf, invoiceNumber))
        }.thenAccept { file ->
            println("Generated successfull!")
        }.thenAccept { DBAccessor.incrementInvoiceNumber(invoiceNumber.toLong()) }
    }
    
    private fun withSuffix(fileName: String, invoiceNumber: String) : String {
        val position = fileName.lastIndexOf(".")
        return fileName.replaceRange(position, position, "_"+
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"))+ "_" + invoiceNumber)
    }
}