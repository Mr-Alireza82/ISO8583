package com.example.sampleproject.core.iso.iso8583.protocol.sipa.header.tpdu

interface TpduBuilder {
    suspend fun build(): TpduReady
}

interface TpduReady {
    suspend fun getBufferId(): ByteArray
    suspend fun getDestinationNii(): ByteArray
    suspend fun getSourceNii(): ByteArray
    suspend fun getTpdu(): ByteArray
}
