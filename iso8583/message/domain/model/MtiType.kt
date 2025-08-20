package com.example.sampleproject.core.iso.iso8583.message.domain.model

import com.example.sampleproject.core.utils.converter.hexToByteArray

enum class MtiType(val code: String) {

    AUTHORIZATION_REQUEST("0100"),
    AUTHORIZATION_RESPONSE("0110"),

    FINANCIAL_TRANSACTION_REQUEST("0200"),
    FINANCIAL_TRANSACTION_RESPONSE("0210"),

    FINANCIAL_TRANSACTION_CONFIGURATION_REQUEST("0220"),
    FINANCIAL_TRANSACTION_CONFIGURATION_RESPONSE("0230"),

    REVERSAL_REQUEST("0420"),
    REVERSAL_RESPONSE("0430"),

    NETWORK_MANAGEMENT_REQUEST("0800"),
    NETWORK_MANAGEMENT_RESPONSE("0810");

    val value: ByteArray get() = code.toByteArray()
    val hexValue: ByteArray get() = code.hexToByteArray()

    companion object {
        fun fromValue(bytes: ByteArray): MtiType? =
            MtiType.entries.find { it.hexValue.contentEquals(bytes) }

        fun fromHex(hex: String): MtiType? =
            MtiType.entries.find { it.code.equals(hex, ignoreCase = true) }
    }
}
