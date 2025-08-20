package com.example.sampleproject.core.iso.iso8583.protocol.sipa.entities

class SipaProtocol() {

    fun getLength(): ByteArray = ByteArray(0)

    fun getId(): ByteArray = ByteArray(0)

    fun getDestinationNii(): ByteArray = ByteArray(0)

    fun getOriginNii(): ByteArray = ByteArray(0)

    fun getTpdu(): ByteArray = ByteArray(0)

    fun getMti(): ByteArray = ByteArray(0)

    fun getBitmap(): ByteArray = ByteArray(0)

    fun getHeader(): ByteArray = ByteArray(0)

    fun getDataElement(field: Int): ByteArray = ByteArray(0)

    fun getBody(): ByteArray = ByteArray(0)

    fun getMessage(): ByteArray = ByteArray(0)
}
