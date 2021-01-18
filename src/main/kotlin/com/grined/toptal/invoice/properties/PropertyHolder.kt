package com.grined.toptal.invoice.properties

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File

object PropertyHolder {
    val applicationConfig: ApplicationConfig by lazy {
        val objectMapper = ObjectMapper(YAMLFactory()).registerKotlinModule()
        objectMapper.readValue<ApplicationConfig>(File("application.yml"))
    }
}
