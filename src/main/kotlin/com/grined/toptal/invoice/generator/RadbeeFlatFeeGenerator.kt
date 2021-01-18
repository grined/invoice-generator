package com.grined.toptal.invoice.generator

import com.grined.toptal.invoice.properties.PropertyHolder
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

object RadbeeFlatFeeGenerator : BasicGenerator() {
    private val config = PropertyHolder.applicationConfig.radbeeFlatFee

    fun generateRadbeeFlatFee(date: LocalDate, invoiceNumber: String) {
        val payMonth = date.getPayMonth()
        val workDateStart = payMonth.with(TemporalAdjusters.firstDayOfMonth())
        val workDateEnd = payMonth.with(TemporalAdjusters.lastDayOfMonth())
        println("Preparing parameters . . .")

        val invoiceInfo = InvoiceConstructor.construct(
            paidDeadlineDuration = config.paidDurationDays,
            invoiceDate = payMonth.withDayOfMonth(28),
            customAmount = "${config.rate}",
            customInvoiceNumber = invoiceNumber,
            workDateStart = workDateStart,
            workDateEnd = workDateEnd,
            additionalDateGap = config.additionalDateGap
        )
        generateDocAndPdf(invoiceInfo, config.template, config.outputDocx, config.outputPdf, invoiceNumber)
    }

    private fun LocalDate.getPayMonth(): LocalDate =
        when {
            this.dayOfMonth <= 10 -> this.minusMonths(1)
            else -> this
        }
}
