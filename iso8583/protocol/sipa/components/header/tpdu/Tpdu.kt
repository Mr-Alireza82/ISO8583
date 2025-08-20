package com.example.sampleproject.core.iso.iso8583.protocol.sipa.components.header.tpdu

import android.app.Application
import com.example.sampleproject.core.database.local.AppDatabase
import com.example.sampleproject.core.di.ApplicationHolder
import com.example.sampleproject.core.iso.iso8583.protocol.sipa.header.tpdu.TpduBuilder
import com.example.sampleproject.core.iso.iso8583.protocol.sipa.header.tpdu.TpduReady
import com.example.sampleproject.core.utils.converter.hexToByteArray
import com.example.sampleproject.features.connection.data.repository.ConnectionRepositoryImpl
import com.example.sampleproject.features.connection.domain.usecase.ConnectionUseCase

class Tpdu() : TpduBuilder, TpduReady {

    private val bufferId: String = "60"
    private val sourceNii: String = "91FE"
    private lateinit var destinationNii: String

    override suspend fun build(): TpduReady {

        val context = ApplicationHolder.applicationContext as Application
        val db = AppDatabase.getInstance(context)
        val dao = db.connectionDao()
        val repo = ConnectionRepositoryImpl(dao)
        val useCase = ConnectionUseCase(repo)

        destinationNii = useCase.getNii()
            ?.toString()
            ?.padStart(4, '0')
            ?.uppercase()
            ?: error("NII not found")

        require(destinationNii.length == 4) { "Invalid NII: $destinationNii" }

        return this
    }

    override suspend fun getBufferId(): ByteArray {
        return bufferId.hexToByteArray()
    }

    override suspend fun getDestinationNii(): ByteArray {
        return destinationNii.hexToByteArray()
    }

    override suspend fun getSourceNii(): ByteArray {
        return sourceNii.hexToByteArray()
    }

    override suspend fun getTpdu(): ByteArray {
        val tpdu = bufferId + sourceNii + destinationNii
        return tpdu.hexToByteArray()
    }
}
