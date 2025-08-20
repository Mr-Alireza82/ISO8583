package com.example.sampleproject.core.iso.iso8583.protocol.sipa.components.body.fields.de48_additionalData

import com.example.sampleproject.core.iso.iso8583.message.domain.model.MessageTypeKey
import com.example.sampleproject.core.utils.converter.asciiToHex
import com.example.sampleproject.core.utils.converter.hexToByteArray

object AdditionalData {

    private val dataByMessageType: Map<MessageTypeKey, Map<String, String>> = mapOf(
        MessageTypeKey("0800".hexToByteArray(), "920000".hexToByteArray()) to mapOf(
            "01" to "99887766",   // Tag 01 : Serial of terminal number
            "02" to "1.9.2",      // Tag 02 : Terminal version
            "03" to "0",          // Tag 03 : Terminal Language
            "15" to "4"           // Tag 15 : Connection type
        ),
        MessageTypeKey("0200".hexToByteArray(), "000000".hexToByteArray()) to mapOf(
            "10" to "CustomerNote",
            "20" to "LoyaltyTag"
        )
    )

    fun getAdditionalData(mti: ByteArray, processCode: ByteArray): ByteArray {
        val messageKey = MessageTypeKey(mti, processCode)
        val fieldMap = dataByMessageType[messageKey]
            ?: error("No DE48 data defined for MTI ${mti.decodeToString()} and Process Code ${processCode.decodeToString()}")

        val ltvBlocks = fieldMap.entries.joinToString("") { (tag, value) ->
            val valueHex = value.asciiToHex()
            val fullTagValue = tag + valueHex
            val length = (fullTagValue.length / 2).toString().padStart(2, '0')
            length + fullTagValue
        }

        val totalLength = (ltvBlocks.length / 2).toString().padStart(4, '0')
        val finalHex = totalLength + ltvBlocks

        return finalHex.hexToByteArray()
    }
}
