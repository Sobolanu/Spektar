package com.example.spektar.ui.settingsScreen.themeScreen

data class ThemeUiState(
    val dynamicColorEnabled: Boolean = false,
    val darkSchemeEnabled: Boolean = false,
    val reduceMotionEnabled: Boolean = false,

    val dynamicColorPreview: Boolean = false,
    val darkSchemePreview: Boolean = false,

    val dynamicColorInfoBox: Boolean = false,
    val darkSchemeInfoBox: Boolean = false,
    val reduceMotionInfoBox: Boolean = false
)

