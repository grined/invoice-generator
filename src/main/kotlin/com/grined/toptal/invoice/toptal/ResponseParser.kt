package com.grined.toptal.invoice.toptal

import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object ResponseParser {
    fun extractInvoiceInfo(
            rawHtml : String,
            useInvoiceDate : Boolean = true,
            manualDateDeadline: LocalDate = LocalDate.MIN) : InvoiceInfo {
        val parsedInvoiceInfo = parse(rawHtml)
        val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)
        if (useInvoiceDate){
            val dateDeadline = LocalDate.parse(parsedInvoiceInfo.date, formatter).plusDays(20)
            return parsedInvoiceInfo.copy(dateDeadline = formatter.format(dateDeadline))
        } else {
            val date = manualDateDeadline.minusDays(20)
            return parsedInvoiceInfo.copy(date = formatter.format(date),
                    dateDeadline = formatter.format(manualDateDeadline))
        }
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