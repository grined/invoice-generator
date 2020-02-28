package com.grined.toptal.invoice.generator

import com.grined.toptal.invoice.cli.askAndGetIfUserApproved
import com.grined.toptal.invoice.data.DBAccessor
import com.grined.toptal.invoice.properties.PropertyHolder
import java.time.DayOfWeek.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

object RadbeeGenerator {
    private val config = PropertyHolder.applicationConfig.radbee

    fun generateRadbee(date: LocalDate, invoiceNumber: String, hours: Int) {
        val workDateEnd: LocalDate = if (date.dayOfWeek in setOf(FRIDAY, SATURDAY, SUNDAY)) {
            date.with(TemporalAdjusters.nextOrSame(SUNDAY))
        } else {
            date.with(TemporalAdjusters.previous(SUNDAY))
        }
        val workDateStart: LocalDate = workDateEnd
                .with(TemporalAdjusters.previous(SUNDAY))
                .with(TemporalAdjusters.previous(MONDAY))
        println("Preparing parameters . . .")

        val invoiceInfo = InvoiceConstructor.construct(
                moneyAlreadyReceived = false,
                paidDeadlineDuration = config.paidDurationDays,
                invoiceDate = date,
                customAmount = "${hours * PropertyHolder.applicationConfig.radbee.rate}",
                customInvoiceNumber = invoiceNumber,
                hours = hours,
                workDateStart = workDateStart,
                workDateEnd = workDateEnd
        )
        takeIf { askAndGetIfUserApproved(invoiceInfo) } ?: return
        println("Success. Generating docx . . .")
        val generatedDoc =
                DocGenerator.generateDoc(invoiceInfo, config.template, withSuffix(config.outputDocx, invoiceNumber))

        println("Success. Generating pdf . . .")
        PdfGenerator.buildPdf(generatedDoc, withSuffix(config.outputPdf, invoiceNumber))

        println("Generated successfull!")
        DBAccessor.incrementInvoiceNumber(invoiceNumber.toLong())
    }

    private fun withSuffix(fileName: String, invoiceNumber: String): String {
        val position = fileName.lastIndexOf(".")
        return fileName.replaceRange(position, position, "_" +
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd")) + "_" + invoiceNumber)
    }
}
