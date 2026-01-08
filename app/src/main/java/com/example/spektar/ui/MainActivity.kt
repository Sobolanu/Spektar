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
import com.example.spektar.data.repository.BookRepository
import com.example.spektar.data.repository.CategoryRepository
import com.example.spektar.data.repository.GameRepository
import com.example.spektar.data.repository.MovieRepository
import com.example.spektar.data.repository.ShowRepository
import com.example.spektar.ui.viewModels.DataStoreViewModel
import com.example.spektar.ui.viewModels.DataStoreViewModelFactory
import com.example.spektar.ui.viewModels.MediaViewModel
import com.example.spektar.ui.viewModels.MediaViewModelFactory
import com.example.spektar.ui.viewModels.SignInViewModel
import com.example.spektar.ui.viewModels.SignInViewModelFactory
import com.example.spektar.ui.viewModels.SignUpViewModel
import com.example.spektar.ui.viewModels.SignUpViewModelFactory
import com.example.spektar.domain.usecase.AccountServiceImpl
import com.example.spektar.ui.navigation.SpektarNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mediaViewModel: MediaViewModel by viewModels {
                MediaViewModelFactory(
                    CategoryRepository(),
                    ShowRepository(),
                    BookRepository(),
                    GameRepository(),
                    MovieRepository()
                )
            }

            val signInViewModel: SignInViewModel by viewModels {
                SignInViewModelFactory(accountService = AccountServiceImpl())
            }

            val signUpViewModel: SignUpViewModel by viewModels {
                SignUpViewModelFactory(accountService = AccountServiceImpl())
            }

            val dataStoreViewModel : DataStoreViewModel by viewModels {DataStoreViewModelFactory(applicationContext.dataStore)}

            val dynamicColorState by dataStoreViewModel.read("dynamic_color").collectAsState(initial = false)
            val darkThemeState by dataStoreViewModel.read("dark_scheme").collectAsState(initial = isSystemInDarkTheme())

            SpektarTheme(
                dynamicColor = dynamicColorState,
                darkTheme = darkThemeState
            ) {
                SpektarNavigation(
                    mediaViewModel,
                    signInViewModel,
                    signUpViewModel,
                    dataStoreViewModel
                )
            }
        }
    }
}