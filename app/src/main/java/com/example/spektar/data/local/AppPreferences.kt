package com.example.spektar.data.local

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

object DataStore {
    val Context.dataStore by preferencesDataStore(name = "settings")
}