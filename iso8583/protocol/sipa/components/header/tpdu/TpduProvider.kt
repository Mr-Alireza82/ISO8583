package com.example.sampleproject.core.iso.iso8583.protocol.sipa.components.header.tpdu


import android.app.Application
import com.example.sampleproject.core.database.local.AppDatabase
import com.example.sampleproject.core.di.ApplicationHolder
import com.example.sampleproject.features.connection.data.repository.ConnectionRepositoryImpl
import com.example.sampleproject.features.connection.domain.usecase.ConnectionUseCase

object TpduProvider {
    suspend fun getTpdu(): ByteArray {
        val context = ApplicationHolder.applicationContext as Application
        val db = AppDatabase.getInstance(context)
        val dao = db.connectionDao()
        val repo = ConnectionRepositoryImpl(dao)
        val useCase = ConnectionUseCase(repo)
        return Tpdu().build().getTpdu()
    }
}
