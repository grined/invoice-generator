package com.grined.toptal.invoice.generator

import com.grined.toptal.invoice.properties.PropertyHolder
import java.time.LocalDate

object ToptalGenerator : BasicGenerator(withInvoiceNumber = false) {
    private val config = PropertyHolder.applicationConfig.toptal

    fun generateToptal(date: LocalDate, amount: String, invoiceNumber: String, title: String) {
        println("Creating document . . .")
        val invoiceInfo = InvoiceConstructor.construct(
            moneyAlreadyReceived = true,
            paidDeadlineDuration = config.paidDurationDays,
            invoiceDate = date,
            customAmount = amount,
            customInvoiceNumber = invoiceNumber,
            customTitle = title
        )

        generateDocAndPdf(invoiceInfo, config.template, config.outputDocx, config.outputPdf, invoiceNumber)
    }
}