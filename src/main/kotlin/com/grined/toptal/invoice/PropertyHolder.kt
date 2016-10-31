package com.grined.toptal.invoice

import java.io.FileInputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object PropertyHolder {
    val prop = Properties()

    init {
        prop.load(FileInputStream("application.properties"))
    }

    fun getProperty(name : String) = prop.getProperty(name)!!

    fun getOutputFileName(reportType: ReportType): String {
        val path = getProperty(reportType.property)
        val position = path.lastIndexOf(".")
        return path.replaceRange(position, position, "_"+
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd")))
    }

    enum class ReportType(val property: String) {
        PDF("output.pdf"), DOCX("output.docx")
    }
}
