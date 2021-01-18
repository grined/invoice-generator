package com.grined.toptal.invoice.generator

import com.grined.toptal.invoice.properties.PropertyHolder
import java.time.DayOfWeek.*
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

object RadbeeGenerator : BasicGenerator() {
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
            paidDeadlineDuration = config.paidDurationDays,
            invoiceDate = date,
            customAmount = "${hours * config.rate}",
            customInvoiceNumber = invoiceNumber,
            hours = hours,
            workDateStart = workDateStart,
            workDateEnd = workDateEnd,
            additionalDateGap = config.additionalDateGap
        )
        generateDocAndPdf(invoiceInfo, config.template, config.outputDocx, config.outputPdf, invoiceNumber)
    }
}
