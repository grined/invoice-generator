package com.grined.toptal.invoice.properties


data class RadbeeExtraConfig (
        val startingInvoiceNumber: Long,
        val paidDurationDays: Long,
        val template: String,
        val outputDocx: String,
        val outputPdf: String)
