package com.example.sampleproject.core.iso.iso8583.protocol.sipa.domain.entity.fields.stan.domain.entity

import android.content.Context
import com.example.sampleproject.core.iso.iso8583.protocol.sipa.domain.entity.fields.stan.data.local.StanDatastore

class Stan(private val context: Context) {
    suspend fun getStan(): String = StanDatastore.getAndIncrement(context)
}
