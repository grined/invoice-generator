package com.grined.toptal.invoice.data

import com.grined.toptal.invoice.properties.PropertyHolder
import java.io.File

object DBAccessor {
    private val startingInvoiceNumber = PropertyHolder.applicationConfig.radbee.startingInvoiceNumber
    private val databaseFile = File("database.db")


    fun currentInvoiceNumber(): String {
        if (databaseFile.createNewFile()) {
            writeValue(startingInvoiceNumber)
        }
        return readCurrentValue().toString()
    }

    fun incrementInvoiceNumber(currentInvoiceNumber: Long) = writeValue(currentInvoiceNumber + 1)

    private fun readCurrentValue(): Long =
        databaseFile.reader().use { input ->
            return input.readText().toLong()
        }

    private fun writeValue(invoiceNumber: Long) =
        databaseFile.printWriter().use { out ->
            out.write(invoiceNumber.toString())
        }

}
