package com.grined.toptal.invoice

import com.grined.toptal.invoice.generator.DocGenerator
import com.grined.toptal.invoice.toptal.ResponseParser
import com.grined.toptal.invoice.toptal.ToptalAccessor
import java.io.FileReader

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
//            SwingUtilities.invokeLater({createGUI()})
            val toptalAccessor = ToptalAccessor()
//            println(toptalAccessor.getInvoice(url))

            val invoiceInfo = ResponseParser.extractInvoiceInfo(FileReader("test.data").readText())
            DocGenerator.generateDoc(invoiceInfo)
        }


    }
}
