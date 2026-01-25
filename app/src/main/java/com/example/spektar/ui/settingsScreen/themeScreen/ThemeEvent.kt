package com.example.spektar.ui.settingsScreen.themeScreen

import android.content.res.Resources

sealed interface ThemeEvent {
    data class dynamicColorToggle(val state: Boolean) : ThemeEvent
    data class darkModeToggle(val state: Boolean): ThemeEvent
    data class reduceMotionToggle(val state: Boolean): ThemeEvent

    data class dynamicColorPreview(val state: Boolean): ThemeEvent

    data class darkModePreview(val state: Boolean): ThemeEvent

    data class dynamicColorInfoBox(val state: Boolean): ThemeEvent
    data class darkModeInfoBox(val state: Boolean): ThemeEvent
    data class reduceMotionToggleInfoBox(val state: Boolean): ThemeEvent
}