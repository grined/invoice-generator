package com.grined.toptal.invoice.generator

import com.grined.toptal.invoice.PropertyHolder
import com.grined.toptal.invoice.toptal.InvoiceInfo
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object InvoiceConstructor {
    fun construct(
            invoiceInfo: InvoiceInfo,
            minusCommission : Boolean = true,
            useInvoiceDate : Boolean = true,
            manualDateDeadline: LocalDate = LocalDate.MIN,
            useCustomAmount: Boolean = false,
            customAmount: String = "",
            useCustomTitle: Boolean = false,
            customTitle: String = "",
            useCustomInvoiceNumber: Boolean = false,
            customInvoiceNumber: String ="") : InvoiceInfo {
        var currentInfo = invoiceInfo
        val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)
        if (useInvoiceDate){
            val dateDeadline = LocalDate.parse(currentInfo.date, formatter).plusDays(20)
            currentInfo = currentInfo.copy(dateDeadline = formatter.format(dateDeadline))
        } else {
            val date = manualDateDeadline.minusDays(20)
            currentInfo = currentInfo.copy(date = formatter.format(date),
                    dateDeadline = formatter.format(manualDateDeadline))
        }

        val moneyFormatter = NumberFormat.getCurrencyInstance(Locale.US)
        if (useCustomAmount) {
            val amount = customAmount.trim().replace(" ","").replace("$","").replace(",",".").toDouble()
            currentInfo = currentInfo.copy(amount = moneyFormatter.format(amount))
        }
        if (minusCommission){
            val commission = PropertyHolder.getProperty("commission").toLong()
            val amount = moneyFormatter.parse(currentInfo.amount).toDouble().minus(commission)
            currentInfo = currentInfo.copy(amount = moneyFormatter.format(amount))
        }
        if (useCustomTitle) {
            currentInfo = currentInfo.copy(title = customTitle)
        }
        if (useCustomInvoiceNumber) {
            currentInfo = currentInfo.copy(number = customInvoiceNumber)
        }

        return currentInfo
    }
}