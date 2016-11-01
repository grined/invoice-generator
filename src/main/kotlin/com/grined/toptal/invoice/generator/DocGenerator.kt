package com.grined.toptal.invoice.generator

import com.grined.toptal.invoice.PropertyHolder
import com.grined.toptal.invoice.PropertyHolder.ReportType.DOCX
import com.grined.toptal.invoice.toptal.InvoiceInfo
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.poi.xwpf.usermodel.XWPFParagraph
import java.io.FileInputStream
import java.io.FileOutputStream

object DocGenerator {
    fun generateDoc(invoiceInfo: InvoiceInfo) : String {
        val document = XWPFDocument(FileInputStream(PropertyHolder.getProperty("template")))
        val tablesParagraphs = document.tables
                .flatMap { t -> t.rows.flatMap { r -> r.tableCells.flatMap { c -> c.paragraphs } } }
        val allParagraphs = tablesParagraphs
                .plus(document.paragraphs)
        allParagraphs.forEach { p -> changeText(p, buildReplacementMap(invoiceInfo)) }
        val outputDOCXFileName = PropertyHolder.getOutputFileName(DOCX)
        document.write(FileOutputStream(outputDOCXFileName))
        return outputDOCXFileName
    }

    private fun buildReplacementMap(invoiceInfo: InvoiceInfo): Map<String, String> =
            hashMapOf("{{amount}}" to invoiceInfo.amount,
                    "{{date}}" to invoiceInfo.date,
                    "{{dateDeadline}}" to invoiceInfo.dateDeadline,
                    "{{title}}" to invoiceInfo.title,
                    "{{invoiceNumber}}" to invoiceInfo.number)

    fun changeText(p: XWPFParagraph, replacementMap: Map<String, String>) {
        val runs = p.runs
        if (runs == null || runs.isEmpty()) {
            return
        }
        val computedRun = runs.map { r -> r.getText(0) }.filterNotNull().joinToString("")
        var result = computedRun
        replacementMap.entries.forEach {e ->
            result = result.replace(e.key, e.value)
        }

        if (!result.equals(computedRun)){
            for (k in runs.size - 1 downTo 1)
                p.removeRun(k)

            val run = runs[0]
            run.setText(result, 0)
        }
    }
}