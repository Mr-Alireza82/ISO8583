package com.example.sampleproject.core.iso8583.client

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.sampleproject.core.network.tcp.data.TCP
import java.util.concurrent.TimeUnit

class Client private constructor() {

    private var ip: String? = null
    private var port: Int? = null
    private var buffer: ByteArray? = null
    private var timeoutMillis: Long? = null
    private var retryCount: Int? = null
    private var enableSSL: Boolean? = null
    private var hasResponseLength: Pair<Boolean, Int>? = null
    private var enableChunk: Boolean? = null

    private val calledMethods = mutableSetOf<RequiredStep>()

    enum class RequiredStep {
        CONFIG,
        BUFFER,
        TIMEOUT,
        RETRY_COUNT,
        ENABLE_SSL,
        RESPONSE_LENGTH,
        CHUNK_RESPONSE
    }

    companion object {
        private val REQUIRED_STEPS = RequiredStep.entries.toSet()

        fun create(): Client = Client()
    }

    fun config(ip: String, port: Int): Client {
        this.ip = ip
        this.port = port
        calledMethods += RequiredStep.CONFIG
        return this
    }

    fun buffer(msg: ByteArray): Client {
        this.buffer = msg
        calledMethods += RequiredStep.BUFFER
        return this
    }

    fun timeout(duration: Long, unit: TimeUnit): Client {
        this.timeoutMillis = unit.toMillis(duration)
        calledMethods += RequiredStep.TIMEOUT
        return this
    }

    fun retryCount(count: Int): Client {
        this.retryCount = count
        calledMethods += RequiredStep.RETRY_COUNT
        return this
    }

    fun enableSSL(required: Boolean): Client {
        this.enableSSL = required
        calledMethods += RequiredStep.ENABLE_SSL
        return this
    }

    fun hasResponseLength(required: Boolean, length: Int): Client {
        this.hasResponseLength = Pair(required, length)
        calledMethods += RequiredStep.RESPONSE_LENGTH
        return this
    }

    fun enableChunkResponse(enable: Boolean): Client {
        this.enableChunk = enable
        calledMethods += RequiredStep.CHUNK_RESPONSE
        return this
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun build(): TCP {
        val missing = REQUIRED_STEPS - calledMethods
        if (missing.isNotEmpty()) {
            throw IllegalStateException("Missing configuration steps: ${missing.joinToString()}")
        }

        return TCP(
            ip!!,
            port!!,
            buffer!!,
            timeoutMillis!!,
            retryCount!!,
            enableSSL == true,
            hasResponseLength!!,
            enableChunk == true
        )
    }
}