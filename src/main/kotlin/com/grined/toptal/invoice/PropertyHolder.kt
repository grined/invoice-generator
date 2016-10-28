package com.grined.toptal.invoice

import java.io.FileInputStream
import java.util.*

object PropertyHolder {
    val prop = Properties()

    init {
        prop.load(FileInputStream("application.properties"))
    }

    fun getProperty(name : String) = prop.getProperty(name)!!
}
