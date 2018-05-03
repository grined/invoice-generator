package com.grined.toptal.invoice.properties


data class RadbeeConfig (
        val startingInvoiceNumber: Long,
        val rate: Int,
        val paidDurationDays: Long,
        val template: String,
        val outputDocx: String,
        val outputPdf: String)
