package com.example.spektar.ui.settingsScreen

sealed interface SettingsEvent {
    data class SelectLanguage(val language: String) : SettingsEvent
}