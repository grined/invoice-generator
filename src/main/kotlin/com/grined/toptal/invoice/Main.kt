package com.grined.toptal.invoice

import com.grined.toptal.invoice.cli.SetupCommand
import com.grined.toptal.invoice.data.DBAccessor
import com.grined.toptal.invoice.generator.GeneratorType.*
import com.grined.toptal.invoice.generator.RadbeeGenerator
import picocli.CommandLine


fun main(args: Array<String>) {
    val setupCommand = SetupCommand()
    CommandLine(setupCommand).execute(*args)
    println("Starting generating document for ${setupCommand.type}")
    when (setupCommand.type) {
        RADBEE -> generateRadbee(setupCommand)
        TOPTAL -> TODO()
        RADBEE_EXTRA -> TODO()
        NONE -> TODO()
    }
}

fun generateRadbee(setupCommand: SetupCommand) {
    RadbeeGenerator.generateRadbee(
            date = setupCommand.date,
            invoiceNumber = setupCommand.invoiceNumber ?: DBAccessor.currentInvoiceNumber(),
            hours = setupCommand.hours
    )
}
