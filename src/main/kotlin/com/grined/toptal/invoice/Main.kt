package com.grined.toptal.invoice

import com.grined.toptal.invoice.cli.SetupCommand
import com.grined.toptal.invoice.data.DBAccessor
import com.grined.toptal.invoice.generator.GeneratorType.*
import com.grined.toptal.invoice.generator.RadbeeExtraGenerator
import com.grined.toptal.invoice.generator.RadbeeFlatFeeGenerator
import com.grined.toptal.invoice.generator.RadbeeGenerator
import com.grined.toptal.invoice.generator.ToptalGenerator
import picocli.CommandLine


fun main(args: Array<String>) {
    val setupCommand = SetupCommand()
    CommandLine(setupCommand).execute(*args)
    println("Starting generating document for ${setupCommand.type}")
    when (setupCommand.type) {
        RADBEE -> RadbeeGenerator.generateRadbee(
            date = setupCommand.date,
            invoiceNumber = setupCommand.invoiceNumber ?: DBAccessor.currentInvoiceNumber(),
            hours = setupCommand.hours
        )
        TOPTAL -> ToptalGenerator.generateToptal(
            date = setupCommand.date,
            amount = setupCommand.amount.toString(),
            invoiceNumber = setupCommand.invoiceNumber!!,
            title = setupCommand.title
        )
        RADBEE_EXTRA -> RadbeeExtraGenerator.generateRadbeeExtra(
            date = setupCommand.date,
            amount = setupCommand.amount.toString(),
            invoiceNumber = setupCommand.invoiceNumber ?: DBAccessor.currentInvoiceNumber()
        )
        RADBEE_FLAT_FEE -> RadbeeFlatFeeGenerator.generateRadbeeFlatFee(
            date = setupCommand.date,
            invoiceNumber = setupCommand.invoiceNumber ?: DBAccessor.currentInvoiceNumber()
        )
        NONE -> TODO()
    }
}

