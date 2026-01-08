package com.example.spektar.ui.viewModels

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// this is a viewmodel so it shouldn't be in models but my project is a mess anyway
// it stores boolean values for now for theme settings in ThemeScreen.
class DataStoreViewModel(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    val data : Flow<Boolean>
        get() {
            return read("Settings")
        }

    suspend fun save(key: String, value: Boolean) {
        val dataStoreKey = booleanPreferencesKey(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    fun read(key: String) : Flow<Boolean> {
        val dataStoreKey = booleanPreferencesKey(key)
        return dataStore.data.map {prefs ->
            prefs[dataStoreKey] ?: false
        }
    }
}

@Suppress("UNCHECKED_CAST")
class DataStoreViewModelFactory(
    private val dataStore: DataStore<Preferences>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DataStoreViewModel::class.java)) {
            return DataStoreViewModel(dataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}