package com.example.spektar.ui.settingsScreen

sealed interface SettingsEvent {
    data class readThemeSetting(val key: String) : SettingsEvent
    data class saveThemeSetting(val key: String, val value: Boolean) : SettingsEvent
}