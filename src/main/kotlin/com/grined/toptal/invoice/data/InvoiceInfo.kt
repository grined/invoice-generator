package com.grined.toptal.invoice.data

data class InvoiceInfo(
    val number: String = "",
    val date: String = "",
    val dateDeadline: String = "",
    val amount: String = "",
    val title: String = "",
    val hours: String = "",
    val workDateStart: String = "",
    val workDateEnd: String = ""
)
