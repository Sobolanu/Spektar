package com.example.spektar.screens.settingsScreen.themeScreen

import androidx.lifecycle.ViewModel

class ThemeScreenViewModel() : ViewModel() {

    fun toggleDynamicColor(dynamicColorState : Boolean) {
        if(dynamicColorState) {
            // toggle on
        } else {
            // toggle off
        }
    }

    fun toggleLightOrDarkMode(lightOrDarkModeState: Boolean) {
        if(lightOrDarkModeState) {
            // toggle dark
        } else {
            // toggle light
        }
    }

}