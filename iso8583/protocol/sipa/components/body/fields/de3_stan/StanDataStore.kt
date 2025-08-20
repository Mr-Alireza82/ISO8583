package com.example.sampleproject.core.iso.iso8583.protocol.sipa.domain.entity.fields.stan.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private const val STAN_PREF_NAME = "iso_stan_prefs"
private val Context.dataStore by preferencesDataStore(STAN_PREF_NAME)

object StanDatastore {
    private val STAN_KEY = intPreferencesKey("trace_number")

    suspend fun getAndIncrement(context: Context): String {
        val current = context.dataStore.data.first()[STAN_KEY] ?: 1
        val next = if (current >= 999999) 1 else current + 1

        context.dataStore.edit { prefs ->
            prefs[STAN_KEY] = next
        }

        return current.toString().padStart(6, '0')
    }
}
