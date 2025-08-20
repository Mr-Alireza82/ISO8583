package com.example.sampleproject.core.iso.iso8583.message.domain.model

import com.example.sampleproject.core.utils.converter.hexToByteArray

object DefaultFieldMapping {

    val defaultFields: Map<MessageTypeKey, List<Int>> = mapOf(
        MessageTypeKey("0200".hexToByteArray(), "000000".hexToByteArray()) to listOf(2, 3, 4, 7, 11, 41, 49),
        MessageTypeKey("0200".hexToByteArray(), "200000".hexToByteArray()) to listOf(2, 3, 4, 11, 41, 90),
        MessageTypeKey("0800".hexToByteArray(), "920000".hexToByteArray()) to listOf(3, 11, 12, 13, 24, 48)
    )

    fun getFieldsFor(mti: ByteArray, processCode: ByteArray): List<Int> {
        return defaultFields[MessageTypeKey(mti, processCode)] ?: emptyList()
    }
}
