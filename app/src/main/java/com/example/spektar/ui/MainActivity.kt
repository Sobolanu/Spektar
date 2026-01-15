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
import com.example.spektar.domain.usecase.AccountServiceImpl
import com.example.spektar.domain.usecase.MediaServiceImpl
import com.example.spektar.ui.navigation.SpektarNavigation
import com.example.spektar.ui.viewModels.DataStoreViewModel
import com.example.spektar.ui.viewModels.DataStoreViewModelFactory
import com.example.spektar.ui.viewModels.MediaViewModel
import com.example.spektar.ui.viewModels.MediaViewModelFactory
import com.example.spektar.ui.viewModels.SignInViewModel
import com.example.spektar.ui.viewModels.SignInViewModelFactory
import com.example.spektar.ui.viewModels.SignUpViewModel
import com.example.spektar.ui.viewModels.SignUpViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val dataStoreViewModel : DataStoreViewModel by viewModels {
                DataStoreViewModelFactory(applicationContext.dataStore)
            }

            val mediaViewModel: MediaViewModel by viewModels {
                MediaViewModelFactory(
                    mediaService = MediaServiceImpl(),
                    accountService = AccountServiceImpl()
                )
            }

            val signInViewModel: SignInViewModel by viewModels {
                SignInViewModelFactory(accountService = AccountServiceImpl())
            }

            val signUpViewModel: SignUpViewModel by viewModels {
                SignUpViewModelFactory(accountService = AccountServiceImpl())
            }

            val dynamicColorState by dataStoreViewModel.readThemeSettings("dynamic_color").collectAsState(initial = false)
            val darkThemeState by dataStoreViewModel.readThemeSettings("dark_scheme").collectAsState(initial = isSystemInDarkTheme())

            SpektarTheme(
                dynamicColor = dynamicColorState,
                darkTheme = darkThemeState
            ) {
                SpektarNavigation(
                    mediaViewModel,
                    signInViewModel,
                    signUpViewModel,
                    dataStoreViewModel,
                )
            }
        }
    }
}