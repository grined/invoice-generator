package com.grined.toptal.invoice.generator

import com.grined.toptal.invoice.PropertyHolder
import com.grined.toptal.invoice.PropertyHolder.ReportType.PDF
import fr.opensagres.xdocreport.converter.ConverterRegistry
import fr.opensagres.xdocreport.converter.ConverterTypeTo
import fr.opensagres.xdocreport.converter.ConverterTypeVia
import fr.opensagres.xdocreport.converter.Options
import fr.opensagres.xdocreport.core.document.DocumentKind
import java.io.FileInputStream
import java.io.FileOutputStream

object PdfGenerator {

    fun buildPdf(docxFilename : String){
        val options = Options.getFrom(DocumentKind.DOCX).to(ConverterTypeTo.PDF).via(ConverterTypeVia.XWPF)
        val converter = ConverterRegistry.getRegistry().getConverter(options)

        converter.convert(FileInputStream(docxFilename),
                FileOutputStream(PropertyHolder.getOutputFileName(PDF)), options)

    }
}