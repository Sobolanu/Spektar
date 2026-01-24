package com.example.spektar.ui.settingsScreen.themeScreen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    private val _dynamicColorInfoBox = MutableStateFlow(false)
    private val _darkModeInfoBox = MutableStateFlow(false)
    private val _reduceMotionInfoBox = MutableStateFlow(false)

    init {

    }

    val uiState = combine(
        readThemeSettings("dynamic_color"),
        readThemeSettings("dark_scheme"),
        readThemeSettings("reduce_motion"),
        _dynamicColorInfoBox,
        _darkModeInfoBox,
        _reduceMotionInfoBox
    ) {
        ThemeUiState(
            dynamicColorEnabled = it[0],
            darkSchemeEnabled = it[1],
            reduceMotionEnabled = it[2],
            dynamicColorInfoBox = it[3],
            darkSchemeInfoBox = it[4],
            reduceMotionInfoBox = it[5]
        )
    }.stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = ThemeUiState())

    fun onEvent(event: ThemeEvent) {
        when(event) {
            is ThemeEvent.darkModeInfoBox -> {
                _darkModeInfoBox.value = event.state
            }

            is ThemeEvent.dynamicColorInfoBox -> {
                _dynamicColorInfoBox.value = event.state
            }

            is ThemeEvent.reduceMotionToggleInfoBox -> {
                _reduceMotionInfoBox.value = event.state
            }

            is ThemeEvent.darkModeToggle -> {
                saveThemeSetting("dark_scheme", event.state)
            }

            is ThemeEvent.dynamicColorToggle -> {
                saveThemeSetting("dynamic_color", event.state)
            }

            is ThemeEvent.reduceMotionToggle -> {
                saveThemeSetting("reduce_motion", event.state)
            }
        }
    }

    fun saveThemeSetting(key: String, value: Boolean) {
        viewModelScope.launch {
            saveThemeSettings(key, value)
        }
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
}

@Suppress("UNCHECKED_CAST")
class ThemeViewModelFactory(
    private val dataStore: DataStore<Preferences>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThemeViewModel::class.java)) {
            return ThemeViewModel(
                dataStore
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}