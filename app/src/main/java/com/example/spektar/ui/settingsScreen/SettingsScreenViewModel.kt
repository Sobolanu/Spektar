package com.example.spektar.ui.settingsScreen

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsViewModel(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    object PreferencesKeys { val LANGUAGE = stringPreferencesKey("language") }
    val savedLanguage: Flow<String> = dataStore.data.map { prefs ->
        prefs[PreferencesKeys.LANGUAGE] ?: "en"
    }

    suspend fun saveLanguagePreferences(lang: String) {
        dataStore.edit { prefs ->
            prefs[PreferencesKeys.LANGUAGE] = lang
        }
    }
}


@Suppress("UNCHECKED_CAST")
class SettingsViewModelFactory(
    private val dataStore: DataStore<Preferences>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(
                dataStore
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}