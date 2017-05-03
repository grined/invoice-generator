package com.grined.toptal.invoice.toptal

import java.io.FileReader

class ToptalAccessorLocal {
    fun getInvoice(url : String) = FileReader("test.data").readText();
}