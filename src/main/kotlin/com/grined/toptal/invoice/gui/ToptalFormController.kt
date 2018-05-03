package com.grined.toptal.invoice.gui

import com.grined.toptal.invoice.DBAccessor
import com.grined.toptal.invoice.generator.RadbeeExtraGenerator
import com.grined.toptal.invoice.generator.RadbeeGenerator
import com.grined.toptal.invoice.generator.ToptalGenerator
import com.grined.toptal.invoice.properties.PropertyHolder
import javafx.fxml.FXML
import javafx.scene.control.*
import java.time.LocalDate


class ToptalFormController {
    @FXML
    lateinit var toptalGenerateButton: Button
    @FXML
    lateinit var toptalDatePicker: DatePicker
    @FXML
    lateinit var toptalTitleField: TextField
    @FXML
    lateinit var toptalStatusLabel: Label
    @FXML
    lateinit var toptalAmountField: TextField
    @FXML
    lateinit var toptalInvoiceNumber: TextField

    @FXML
    lateinit var radbeeGenerateButton: Button
    @FXML
    lateinit var radbeeDatePicker: DatePicker
    @FXML
    lateinit var radbeeStatusLabel: Label
    @FXML
    lateinit var radbeeAmountField: TextField
    @FXML
    lateinit var radbeeInvoiceNumber: TextField
    @FXML
    lateinit var radbeeIgnoreHoursCheckbox: CheckBox
    @FXML
    lateinit var radbeeHoursField: TextField
    @FXML
    lateinit var radbeeRateLabel: Label

    fun initialize() {
        toptalDatePicker.value = LocalDate.now()
        radbeeDatePicker.value = LocalDate.now()
        radbeeAmountField.isDisable = true
        radbeeIgnoreHoursCheckbox.isSelected = false
        radbeeRateLabel.text = " * " +PropertyHolder.applicationConfig.radbee.rate+"$/hr"+" = "
        radbeeInvoiceNumber.text = DBAccessor.currentInvoiceNumber().toString()
        setupActions()
    }

    private fun setupActions() {
        toptalGenerateButton.setOnAction {
            ToptalGenerator.generateToptal(
                    date = toptalDatePicker.value,
                    amount = toptalAmountField.text,
                    invoiceNumber = toptalInvoiceNumber.text,
                    title = toptalTitleField.text,
                    statusLabel = toptalStatusLabel
            )
        }
        radbeeIgnoreHoursCheckbox.setOnAction {
            radbeeAmountField.isDisable = !radbeeIgnoreHoursCheckbox.isSelected
            radbeeHoursField.isDisable = radbeeIgnoreHoursCheckbox.isSelected
            radbeeAmountField.text = ""
            radbeeHoursField.text = ""
            radbeeInvoiceNumber.text = DBAccessor.currentInvoiceNumber().toString()
        }
        radbeeHoursField.setOnAction {
            calculateRadbeeAmount()
            radbeeInvoiceNumber.text = DBAccessor.currentInvoiceNumber().toString()
        }
        radbeeGenerateButton.setOnAction {
            if (!radbeeIgnoreHoursCheckbox.isSelected) {
                calculateRadbeeAmount()
                RadbeeGenerator.generateRadbee(
                        date = radbeeDatePicker.value,
                        hours = radbeeHoursField.text.toInt(),
                        amount = radbeeAmountField.text,
                        invoiceNumber = radbeeInvoiceNumber.text,
                        statusLabel = radbeeStatusLabel
                )
            } else {
                RadbeeExtraGenerator.generateRadbee(
                        date = radbeeDatePicker.value,
                        amount = radbeeAmountField.text,
                        invoiceNumber = radbeeInvoiceNumber.text,
                        statusLabel = radbeeStatusLabel
                )
            }
        }
    }

    private fun calculateRadbeeAmount() {
        radbeeAmountField.text =
                (radbeeHoursField.text.toInt() * PropertyHolder.applicationConfig.radbee.rate).toString()
    }

}


