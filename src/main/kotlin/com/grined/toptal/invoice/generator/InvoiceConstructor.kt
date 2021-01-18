package com.grined.toptal.invoice.generator

import com.grined.toptal.invoice.data.InvoiceInfo
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object InvoiceConstructor {
    private val dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)
    private val moneyFormatter = NumberFormat.getCurrencyInstance(Locale.US)

    fun construct(
        paidDeadlineDuration: Long,
        invoiceDate: LocalDate,
        moneyAlreadyReceived: Boolean = false,
        workDateStart: LocalDate = LocalDate.MIN,
        workDateEnd: LocalDate = LocalDate.MIN,
        customAmount: String = "",
        customTitle: String = "",
        customInvoiceNumber: String = "",
        hours: Int = 0,
        additionalDateGap: Long = 0
    ): InvoiceInfo {
        val currentInfo = buildInvoiceDates(
            moneyAlreadyReceived = moneyAlreadyReceived,
            invoiceDate = invoiceDate,
            paidDeadlineDuration = paidDeadlineDuration,
            additionalGapForInvoiceDates = additionalDateGap
        )

        val amount = customAmount.trim().replace(" ", "").replace("$", "").replace(",", ".").toDouble()

        return currentInfo.copy(
            amount = moneyFormatter.format(amount),
            title = customTitle,
            number = customInvoiceNumber,
            hours = hours.toString(),
            workDateStart = dateFormatter.format(workDateStart),
            workDateEnd = dateFormatter.format(workDateEnd)
        )
    }

    private fun buildInvoiceDates(
        moneyAlreadyReceived: Boolean,
        invoiceDate: LocalDate,
        paidDeadlineDuration: Long,
        additionalGapForInvoiceDates: Long
    ) =
        when {
            moneyAlreadyReceived -> { // usually Toptal case
                val date = invoiceDate.minusDays(paidDeadlineDuration)
                InvoiceInfo(
                    date = dateFormatter.format(date),
                    dateDeadline = dateFormatter.format(invoiceDate)
                )
            }
            else -> {
                val invoiceDatePlusGap = invoiceDate.plusDays(additionalGapForInvoiceDates)
                val dateDeadline = invoiceDatePlusGap.plusDays(paidDeadlineDuration)
                InvoiceInfo(
                    date = dateFormatter.format(invoiceDatePlusGap),
                    dateDeadline = dateFormatter.format(dateDeadline)
                )
            }
        }
}