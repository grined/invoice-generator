package com.grined.toptal.invoice.generator

import com.grined.toptal.invoice.toptal.InvoiceInfo
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object InvoiceConstructor {
    private val dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)
    private val moneyFormatter = NumberFormat.getCurrencyInstance(Locale.US)

    fun construct(
            moneyAlreadyReceived : Boolean,
            paidDeadlineDuration: Long,
            invoiceDate: LocalDate,
            workDateStart: LocalDate = LocalDate.MIN,
            workDateEnd: LocalDate = LocalDate.MIN,
            customAmount: String = "",
            customTitle: String = "",
            customInvoiceNumber: String = "",
            hours: Int = 0
    ) : InvoiceInfo {
        var currentInfo = InvoiceInfo()

        currentInfo = if (!moneyAlreadyReceived){
            val dateDeadline = invoiceDate.plusDays(paidDeadlineDuration)
            currentInfo.copy(
                    date = dateFormatter.format(invoiceDate),
                    dateDeadline = dateFormatter.format(dateDeadline))
        } else {
            val date = invoiceDate.minusDays(paidDeadlineDuration)
            currentInfo.copy(
                    date = dateFormatter.format(date),
                    dateDeadline = dateFormatter.format(invoiceDate))
        }

        val amount = customAmount.trim().replace(" ","").replace("$","").replace(",",".").toDouble()

        currentInfo = currentInfo.copy(
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

        return currentInfo
    }
}