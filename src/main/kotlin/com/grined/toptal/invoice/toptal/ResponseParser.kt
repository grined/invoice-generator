package com.grined.toptal.invoice.toptal

import org.jsoup.Jsoup
import org.jsoup.select.Elements

object ResponseParser {
    fun parse(rawHtml : String) : InvoiceInfo {
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