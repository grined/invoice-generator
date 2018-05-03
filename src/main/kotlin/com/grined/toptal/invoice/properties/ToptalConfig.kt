package com.grined.toptal.invoice.properties


data class ToptalConfig(
        val paidDurationDays: Long,
        val template: String,
        val outputDocx: String,
        val outputPdf: String)
