package com.grined.toptal.invoice.toptal

import com.grined.toptal.invoice.PropertyHolder
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object ResponseParser {
    fun extractInvoiceInfo(
            rawHtml : String,
            minusCommission : Boolean = true,
            useInvoiceDate : Boolean = true,
            manualDateDeadline: LocalDate = LocalDate.MIN,
            useCustomAmount: Boolean = false,
            customAmount: String = "") : InvoiceInfo {
        var currentInfo = parse(rawHtml)
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
            currentInfo = currentInfo.copy(amount = moneyFormatter.format(customAmount.toDouble()))
        }
        if (minusCommission){
            val commission = PropertyHolder.getProperty("commission").toLong()
            val amount = moneyFormatter.parse(currentInfo.amount).toDouble().minus(commission)
            currentInfo = currentInfo.copy(amount = moneyFormatter.format(amount))
        }
        return currentInfo
    }

    private fun parse(rawHtml : String) : InvoiceInfo {
        val doc = Jsoup.parse(rawHtml)
        val contentElement = doc.select("div[id=content]")
        val listOfData = contentElement.select("td")
        val date = findInvoiceDate(listOfData)

        return InvoiceInfo(findInvoiceNumber(listOfData),
                date,
                date,
                findInvoiceAmount(listOfData),
                contentElement.select("td.title").text())
    }

    private fun findInvoiceNumber(listOfData: Elements)
            = listOfData.find { el -> el.text().contains("Invoice #") }!!.text().split("Invoice ")[1].trim()

    private fun findInvoiceDate(listOfData: Elements)
            = listOfData.find { el -> el.text().contains("Date:") }!!.text().split("Date:")[1].trim()

    private fun findInvoiceAmount(listOfData: Elements)
            = listOfData.find { el -> el.text().contains("$") }!!.text()


}