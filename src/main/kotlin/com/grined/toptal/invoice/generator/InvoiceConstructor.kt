package com.grined.toptal.invoice.generator

import com.grined.toptal.invoice.properties.PropertyHolder
import com.grined.toptal.invoice.toptal.InvoiceInfo
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object InvoiceConstructor {
    private val dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)
    private val moneyFormatter = NumberFormat.getCurrencyInstance(Locale.US)

    fun construct(
            moneyAlreadyReceived: Boolean,
            paidDeadlineDuration: Long,
            invoiceDate: LocalDate,
            workDateStart: LocalDate = LocalDate.MIN,
            workDateEnd: LocalDate = LocalDate.MIN,
            customAmount: String = "",
            customTitle: String = "",
            customInvoiceNumber: String = "",
            hours: Int = 0
    ): InvoiceInfo {
        val currentInfo = buildInvoiceDates(
                moneyAlreadyReceived = moneyAlreadyReceived,
                invoiceDate = invoiceDate,
                paidDeadlineDuration = paidDeadlineDuration,
                additionalGapForInvoiceDates = PropertyHolder.applicationConfig.radbee.additionalDateGap
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
//        if (minusCommission){
//            val commission = PropertyHolder.getProperty("commission").toLong()
//            val amount = moneyFormatter.parse(currentInfo.amount).toDouble().minus(commission)
//            currentInfo = currentInfo.copy(amount = moneyFormatter.format(amount))
//        }
    }

    private fun buildInvoiceDates(
            moneyAlreadyReceived: Boolean,
            invoiceDate: LocalDate,
            paidDeadlineDuration: Long,
            additionalGapForInvoiceDates: Long) =
            if (moneyAlreadyReceived) { // usually Toptal case
                val date = invoiceDate.minusDays(paidDeadlineDuration)
                InvoiceInfo(
                        date = dateFormatter.format(date),
                        dateDeadline = dateFormatter.format(invoiceDate))
            } else {
                val invoiceDatePlusGap = invoiceDate.plusDays(additionalGapForInvoiceDates);
                val dateDeadline = invoiceDatePlusGap.plusDays(paidDeadlineDuration)
                InvoiceInfo(
                        date = dateFormatter.format(invoiceDatePlusGap),
                        dateDeadline = dateFormatter.format(dateDeadline))
            }
}