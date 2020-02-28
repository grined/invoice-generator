package com.grined.toptal.invoice.cli

import com.grined.toptal.invoice.generator.GeneratorType
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.time.LocalDate

@Command(name = "invoice-generator-setup", helpCommand = true)
class SetupCommand : Runnable {
    @Option(names = ["-hours"], description = ["Hours"],arity = "0..1", interactive = true)
    var hours: Int = 0
    @Option(names = ["-amount"], description = ["Amount"], arity = "0..1", interactive = true)
    var amount: Double = 0.0
    @Option(names = ["-date"], description = ["Date"], arity = "0..1" ,interactive = true)
    var date: LocalDate = LocalDate.now()
    @Option(names = ["-invoice-number"], description = ["Invoice number"], arity = "0..1" ,interactive = true)
    var invoiceNumber: String? = null
    @Option(names = ["-title"], description = ["Title"], arity = "0..1" ,interactive = true)
    var title: String = ""
    @Option(names = ["-type"], description = ["Generator type"],arity = "0..1" ,interactive = true)
    var type: GeneratorType = GeneratorType.NONE

    override fun run() {
    }
}