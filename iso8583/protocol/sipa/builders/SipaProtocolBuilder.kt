package com.example.sampleproject.core.iso.iso8583.protocol.sipa.builders

import android.util.Log
import com.example.sampleproject.core.di.ApplicationHolder
import com.example.sampleproject.core.iso.iso8583.message.domain.model.DefaultFieldMapping
import com.example.sampleproject.core.iso.iso8583.message.domain.model.MtiType
import com.example.sampleproject.core.iso.iso8583.message.domain.model.ProcessCodeType
import com.example.sampleproject.core.iso.iso8583.protocol.sipa.entities.SipaProtocol
import com.example.sampleproject.core.iso.iso8583.protocol.sipa.components.body.fields.de12_time.Time
import com.example.sampleproject.core.iso.iso8583.protocol.sipa.components.body.fields.de13_date.Date
import com.example.sampleproject.core.iso.iso8583.protocol.sipa.components.body.fields.de48_additionalData.AdditionalData
import com.example.sampleproject.core.iso.iso8583.protocol.sipa.components.header.tpdu.Tpdu
import com.example.sampleproject.core.iso.iso8583.protocol.sipa.domain.entity.fields.stan.domain.entity.Stan
import com.example.sampleproject.core.iso8583.message.domain.entity.Bitmap
import com.example.sampleproject.core.utils.converter.byteArrayToHex
import com.example.sampleproject.core.utils.converter.hexToByteArray

class SipaProtocolBuilder(
    private val mti: ByteArray,
    private val processCode: ByteArray
) {

    private lateinit var tpdu: ByteArray
    private lateinit var bitmap: ByteArray
    private val dataElements: MutableMap<Int, ByteArray> = mutableMapOf()
    private lateinit var messageBody: ByteArray
    private lateinit var finalMessage: ByteArray

    val mtiType: MtiType? = MtiType.fromValue(mti)
    val processCodeType: ProcessCodeType? = ProcessCodeType.fromValue(processCode)

    init {
        require(mti.size == 2) { "MTI must be 2 bytes" }
        require(processCode.size == 3) { "Process Code must be 3 bytes (6 hex digits)" }
    }

    val context = ApplicationHolder.applicationContext

    suspend fun build(): SipaProtocol {
        tpdu = Tpdu().build().getTpdu()

        val activeFields = DefaultFieldMapping.getFieldsFor(mti, processCode)

        bitmap = Bitmap.fromFields(activeFields).toByteArray()

        activeFields.forEach { field ->
            dataElements[field] = buildDefaultValueForField(field)
        }

        messageBody = buildBody(dataElements)

        Log.i("MeSSAGBody", messageBody.byteArrayToHex())

        val message = tpdu + mti + bitmap + messageBody

        val length = message.size
            .toString()
            .padStart(4, '0')
            .toByteArray()

        finalMessage = length + message

        Log.i("FulLMESSage", finalMessage.byteArrayToHex())

        return SipaProtocol()
    }

    private fun buildBody(fields: Map<Int, ByteArray>): ByteArray {
        return fields.toSortedMap().values.fold(ByteArray(0)) { acc, bytes -> acc + bytes }
    }

    private suspend fun buildDefaultValueForField(field: Int): ByteArray {
        return when (field) {
            1, 2, 3 -> ByteArray(0) // Safe skip

            11 -> Stan(context = context).getStan().hexToByteArray()
            12 -> Time.getCurrentTime().hexToByteArray()
            13 -> Date.getCurrentDate() .hexToByteArray()
            24 -> Tpdu().build().getDestinationNii()
            48 -> AdditionalData.getAdditionalData(mti, processCode)

            else -> error("No default implementation defined for ISO8583 field $field")
        }
    }

    override fun toString(): String =
        "SipaProtocolBuilder(mti='${mti.byteArrayToHex()}', processCode='${processCode.byteArrayToHex()}', mtiType=$mtiType, processCodeType=$processCodeType)"

    companion object {

        fun from(mti: Any, processCode: Any): SipaProtocolBuilder {
            val mtiBytes = coerceHexToBytes(mti)
            val processCodeBytes = coerceHexToBytes(processCode)
            return SipaProtocolBuilder(mtiBytes, processCodeBytes)
        }

        private fun coerceHexToBytes(input: Any): ByteArray {
            return when (input) {
                is ByteArray -> input
                is Int -> input.toString().hexToByteArray()
                is String -> input.hexToByteArray()
                is MtiType -> input.hexValue
                is ProcessCodeType -> input.hexValue
                else -> error("Unsupported type for field: ${input::class.simpleName}")
            }
        }
    }
}
