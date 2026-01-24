@file:Suppress("unused")

package com.example.spektar.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.compose.SpektarTheme
import com.example.spektar.data.local.DataStore.dataStore
import com.example.spektar.ui.navigation.SpektarNavigation
import com.example.spektar.ui.settingsScreen.DataStoreViewModel
import com.example.spektar.ui.settingsScreen.DataStoreViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val dataStoreViewModel : DataStoreViewModel by viewModels {
                DataStoreViewModelFactory(applicationContext.dataStore)
            }

            val dynamicColorState by dataStoreViewModel.readThemeSettings("dynamic_color").collectAsState(initial = false)
            val darkThemeState by dataStoreViewModel.readThemeSettings("dark_scheme").collectAsState(initial = isSystemInDarkTheme())

            SpektarTheme(
                dynamicColor = dynamicColorState,
                darkTheme = darkThemeState
            ) {
                SpektarNavigation(
                    dataStoreViewModel = dataStoreViewModel
                )
            }
        }
    }
}