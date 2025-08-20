package com.example.sampleproject.core.iso8583.message.domain.entity

import java.util.BitSet
import kotlin.experimental.or

class Bitmap {

    private var bitSet = BitSet(64)

    fun setField(field: Int) {
        require(field in 1..128) { "Field number must be between 1 and 128" }

        // Upgrade to 128-bit if needed
        if (field > 64 && bitSet.size() < 128) {
            val upgraded = BitSet(128)
            for (i in 0 until bitSet.size()) {
                if (bitSet[i]) upgraded.set(i)
            }
            bitSet = upgraded

            bitSet.set(0) // bit 1 indicates secondary bitmap present
        }

        bitSet.set(field - 1)
    }

    fun isFieldSet(field: Int): Boolean {
        require(field in 1..128)
        return bitSet.get(field - 1)
    }

    fun toByteArray(): ByteArray {
        val size = if (bitSet.get(0)) 16 else 8
        return ByteArray(size).apply {
            for (i in 0 until size * 8) {
                if (bitSet[i]) {
                    this[i / 8] = this[i / 8] or (1 shl (7 - (i % 8))).toByte()
                }
            }
        }
    }

    companion object {
        fun fromFields(fields: List<Int>): Bitmap {
            val bitmap = Bitmap()
            fields.forEach { bitmap.setField(it) }
            return bitmap
        }
    }

    override fun toString(): String {
        return toByteArray().joinToString(" ") { "%02X".format(it) }
    }
}
