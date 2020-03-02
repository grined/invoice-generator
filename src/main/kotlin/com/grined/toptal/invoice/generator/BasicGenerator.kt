package com.grined.toptal.invoice.generator

import com.grined.toptal.invoice.cli.askAndGetIfUserApproved
import com.grined.toptal.invoice.data.DBAccessor
import com.grined.toptal.invoice.data.InvoiceInfo
import java.time.LocalDate
import java.time.format.DateTimeFormatter

abstract class BasicGenerator(private val withInvoiceNumber: Boolean = true) {

    fun generateDocAndPdf(
            invoiceInfo: InvoiceInfo,
            template: String,
            outputDocx: String,
            outputPdf: String,
            invoiceNumber: String
    ) {
        takeIf { askAndGetIfUserApproved(invoiceInfo) } ?: return
        println("Success. Generating docx . . .")
        val generatedDoc = DocGenerator.generateDoc(invoiceInfo, template, withSuffix(outputDocx, invoiceNumber))
        println("Success. Generating pdf . . .")
        PdfGenerator.buildPdf(generatedDoc, withSuffix(outputPdf, invoiceNumber))
        println("Generated successfull!")
        if (withInvoiceNumber) {
            DBAccessor.incrementInvoiceNumber(invoiceNumber.toLong())
        }
    }

    private fun withSuffix(fileName: String, invoiceNumber: String): String {
        val position = fileName.lastIndexOf(".")
        return fileName.replaceRange(position, position, "_" +
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd")) +
                if (withInvoiceNumber) "_$invoiceNumber" else "")
    }

}