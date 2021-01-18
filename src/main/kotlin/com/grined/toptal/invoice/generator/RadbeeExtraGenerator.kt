package com.grined.toptal.invoice.generator

import com.grined.toptal.invoice.properties.PropertyHolder
import java.time.LocalDate

object RadbeeExtraGenerator : BasicGenerator() {
    private val config = PropertyHolder.applicationConfig.radbeeExtra

    fun generateRadbeeExtra(date: LocalDate, amount: String, invoiceNumber: String) {
        println("Preparing parameters . . .")
        val invoiceInfo = InvoiceConstructor.construct(
            paidDeadlineDuration = config.paidDurationDays,
            invoiceDate = date,
            customAmount = amount,
            customInvoiceNumber = invoiceNumber
        )
        generateDocAndPdf(invoiceInfo, config.template, config.outputDocx, config.outputPdf, invoiceNumber)
    }
}