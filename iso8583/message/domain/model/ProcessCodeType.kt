package com.example.sampleproject.core.iso.iso8583.message.domain.model

import com.example.sampleproject.core.utils.converter.hexToByteArray

enum class ProcessCodeType(val code: String) {
    PURCHASE("000000"),
    REFUND("200000"),
    REVERSAL("400000"),
    BALANCE_INQUIRY("310000"),
    PIN_CHANGE("900000");

    val value: ByteArray get() = code.toByteArray()
    val hexValue: ByteArray get() = code.hexToByteArray()

    companion object {
        fun fromValue(bytes: ByteArray): ProcessCodeType? =
            entries.find { it.hexValue.contentEquals(bytes) }

        fun fromHex(hex: String): ProcessCodeType? =
            entries.find { it.code.equals(hex, ignoreCase = true) }
    }
}
