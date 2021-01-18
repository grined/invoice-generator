package com.grined.toptal.invoice.properties


data class ApplicationConfig(
    val toptal: ToptalConfig,
    val radbee: RadbeeConfig,
    val radbeeExtra: RadbeeExtraConfig,
    val radbeeFlatFee: RadbeeFlatFeeConfig
)

data class RadbeeConfig(
    val startingInvoiceNumber: Long,
    val rate: Int,
    val paidDurationDays: Long,
    val template: String,
    val outputDocx: String,
    val outputPdf: String,
    val additionalDateGap: Long = 0
)

data class RadbeeFlatFeeConfig(
    val startingInvoiceNumber: Long,
    val rate: Int,
    val paidDurationDays: Long,
    val template: String,
    val outputDocx: String,
    val outputPdf: String,
    val additionalDateGap: Long = 0
)

data class RadbeeExtraConfig(
    val startingInvoiceNumber: Long,
    val paidDurationDays: Long,
    val template: String,
    val outputDocx: String,
    val outputPdf: String
)

data class ToptalConfig(
    val paidDurationDays: Long,
    val template: String,
    val outputDocx: String,
    val outputPdf: String
)
