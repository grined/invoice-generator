package com.grined.toptal.invoice.cli

import com.grined.toptal.invoice.data.InvoiceInfo
import picocli.CommandLine
import picocli.CommandLine.Command

@Command(name = "invoice-generator-approve", helpCommand = true)
class ApproveCommand : Runnable {
    @CommandLine.Parameters(interactive = true, index = "0", description = ["y/n"])
    var answer: String = "n"
    @CommandLine.Parameters(hidden = true, index = "*")
    var stub: String = ""

    override fun run() {}
}

fun askAndGetIfUserApproved(invoiceInfo: InvoiceInfo) : Boolean {
    println("Data prepared: \n$invoiceInfo")
    val approveCommand = ApproveCommand()
    println("Do you want to generate invoices?")
    CommandLine(approveCommand).execute("")
    return approveCommand.answer in setOf("y", "yes", "Y")
}