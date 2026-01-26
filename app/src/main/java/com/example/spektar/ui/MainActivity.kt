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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.compose.SpektarTheme
import com.example.spektar.data.local.DataStore.dataStore
import com.example.spektar.data.remote.AccountServiceImpl
import com.example.spektar.ui.navigation.SpektarNavigation
import com.example.spektar.ui.settingsScreen.DataStoreViewModel
import com.example.spektar.ui.settingsScreen.DataStoreViewModelFactory

class MainActivity : ComponentActivity() {
    private val viewModel : MainViewModel by viewModels {
        MainViewModelFactory(AccountServiceImpl())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !viewModel.isReady.value
            }
        }

        enableEdgeToEdge()
        setContent {
            // this viewModel is scoped to activity-level as default for theme
            val dataStoreViewModel: DataStoreViewModel by viewModels {
                DataStoreViewModelFactory(applicationContext.dataStore)
            }

            val dynamicColorState by dataStoreViewModel.readThemeSettings("dynamic_color").collectAsState(initial = false)
            val darkThemeState by dataStoreViewModel.readThemeSettings("dark_scheme").collectAsState(initial = isSystemInDarkTheme())

            SpektarTheme(
                dynamicColor = dynamicColorState,
                darkTheme = darkThemeState
            ) {
                SpektarNavigation(
                    viewModel.userAuthenticated.value
                )
            }
        }
    }
}
