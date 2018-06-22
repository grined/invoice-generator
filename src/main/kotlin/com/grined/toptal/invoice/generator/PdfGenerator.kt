package com.grined.toptal.invoice.generator

import fr.opensagres.xdocreport.converter.ConverterRegistry
import fr.opensagres.xdocreport.converter.ConverterTypeTo
import fr.opensagres.xdocreport.converter.Options
import fr.opensagres.xdocreport.core.document.DocumentKind
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object PdfGenerator {

    fun buildPdf(docxFilename : String, outputPDF: String): File {
        val options = Options.getFrom(DocumentKind.DOCX).to(ConverterTypeTo.PDF)
        val converter = ConverterRegistry.getRegistry().getConverter(options)
        converter.convert(FileInputStream(docxFilename), FileOutputStream(outputPDF), options)
        return File(outputPDF)

    }
}