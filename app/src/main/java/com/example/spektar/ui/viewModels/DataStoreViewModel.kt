package com.example.spektar.ui.viewModels

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

// stores data
// functions that read data return values, anything that saves data doesn't return anything
class DataStoreViewModel(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    companion object {
        private val KEY_SETTINGS = booleanPreferencesKey("Settings")
        private val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    // theme settings:

    val themeSetting: Flow<Boolean>
        get() = dataStore.data.map { prefs ->
            prefs[KEY_SETTINGS] ?: false
        }

    fun readThemeSettings(key: String) : Flow<Boolean> {
        val dataStoreKey = booleanPreferencesKey(key)
        return dataStore.data.map { prefs ->
            prefs[dataStoreKey] ?: false
        }
    }

    suspend fun saveThemeSettings(key: String, value: Boolean) {
        val dataStoreKey = booleanPreferencesKey(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    // session storage:

    suspend fun saveRefreshToken(token: String?) {
        dataStore.edit { prefs ->
            if (token == null) {
                prefs.remove(KEY_REFRESH_TOKEN)
            } else {
                prefs[KEY_REFRESH_TOKEN] = token
            }
        }
    }

    fun readRefreshToken(): Flow<String?> =
        dataStore.data.map { prefs ->
            prefs[KEY_REFRESH_TOKEN]
        }

    suspend fun getRefreshTokenOnce(): String? {
        return dataStore.data.map { prefs ->
            prefs[KEY_REFRESH_TOKEN]
        }.firstOrNull()
    }

    suspend fun clearRefreshToken() {
        dataStore.edit { prefs ->
            prefs.remove(KEY_REFRESH_TOKEN)
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