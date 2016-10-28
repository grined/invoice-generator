package com.grined.toptal.invoice.generator

import com.grined.toptal.invoice.PropertyHolder
import com.grined.toptal.invoice.toptal.InvoiceInfo
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.poi.xwpf.usermodel.XWPFParagraph
import java.io.FileInputStream
import java.io.FileOutputStream

object DocGenerator {
    fun generateDoc(invoiceInfo: InvoiceInfo) {
        val document = XWPFDocument(FileInputStream(PropertyHolder.getProperty("template")))
        val tablesParagraphs = document.tables
                .flatMap { t -> t.rows.flatMap { r -> r.tableCells.flatMap { c -> c.paragraphs } } }
        val allParagraphs = tablesParagraphs
                .plus(document.paragraphs)
        allParagraphs.forEach { p -> changeText(p, buildReplacementMap(invoiceInfo)) }
        document.write(FileOutputStream("output.docx"))
    }

    private fun buildReplacementMap(invoiceInfo: InvoiceInfo): Map<String, String> =
            hashMapOf("{{amount}}" to invoiceInfo.amount,
                    "{{date}}" to invoiceInfo.date,
                    "{{dateDeadline}}" to invoiceInfo.dateDeadline,
                    "{{title}}" to invoiceInfo.title,
                    "{{invoiceNumber}}" to invoiceInfo.number)

    private fun changeText(p: XWPFParagraph, replacementMap: Map<String, String>) {
        val runs = p.runs
        if (runs != null) {
            for (r in runs!!) {
                var text = r.getText(0)
                replacementMap.entries.forEach {
                    e ->
                    if (text != null && text!!.contains(e.key)) {
                        text = text!!.replace(e.key, e.value)
                        r.setText(text, 0)
                    }

                }

            }
        }
    }
}