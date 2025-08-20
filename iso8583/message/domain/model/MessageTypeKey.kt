package com.example.sampleproject.core.iso.iso8583.message.domain.model

data class MessageTypeKey(
    val mti: ByteArray,
    val processCode: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MessageTypeKey) return false
        return mti.contentEquals(other.mti) && processCode.contentEquals(other.processCode)
    }

    override fun hashCode(): Int {
        return mti.contentHashCode() * 31 + processCode.contentHashCode()
    }
}
