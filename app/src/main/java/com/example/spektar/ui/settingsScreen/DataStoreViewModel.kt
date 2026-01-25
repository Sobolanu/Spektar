package com.example.spektar.ui.settingsScreen

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// stores data
// functions that read data return values, anything that saves data doesn't return anything
class DataStoreViewModel(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    fun readThemeSettings(key: String): Flow<Boolean> {
        return dataStore.data.map { prefs ->
            when (key) {
                "dark_scheme" -> prefs[booleanPreferencesKey(key)] ?: true // forcing dark mode initially
                "dynamic_color" -> prefs[booleanPreferencesKey(key)] ?: false
                else -> false
            }
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