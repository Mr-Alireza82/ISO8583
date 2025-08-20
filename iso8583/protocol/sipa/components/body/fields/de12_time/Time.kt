package com.example.sampleproject.core.iso.iso8583.protocol.sipa.components.body.fields.de12_time

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Time {

    fun getCurrentTime(pattern: String = "HHmmss"): String {
        return try {
            val formatter = SimpleDateFormat(pattern, Locale.getDefault())
            formatter.format(Date())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid time format pattern: \"$pattern\"", e)
        }
    }
}
