package com.example.sampleproject.core.iso.iso8583.protocol.sipa.components.body.fields.de13_date

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Date {

    fun getCurrentDate(pattern: String = "MMdd"): String {
        return try {
            val formatter = SimpleDateFormat(pattern, Locale.getDefault())
            formatter.format(Date())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid date format pattern: \"$pattern\"", e)
        }
    }
}
