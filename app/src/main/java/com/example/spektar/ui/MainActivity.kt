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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.room.Room
import com.example.compose.SpektarTheme
import com.example.spektar.data.local.DataStore.dataStore
import com.example.spektar.data.local.NoteDatabase
import com.example.spektar.domain.usecase.AccountServiceImpl
import com.example.spektar.domain.usecase.MediaServiceImpl
import com.example.spektar.ui.navigation.SpektarNavigation
import com.example.spektar.ui.viewModels.DataStoreViewModel
import com.example.spektar.ui.viewModels.DataStoreViewModelFactory
import com.example.spektar.ui.viewModels.MediaViewModel
import com.example.spektar.ui.viewModels.MediaViewModelFactory
import com.example.spektar.ui.viewModels.NoteViewModel
import com.example.spektar.ui.viewModels.NoteViewModelFactory
import com.example.spektar.ui.viewModels.ProfileViewModel
import com.example.spektar.ui.viewModels.ProfileViewModelFactory
import kotlin.getValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val profileViewModel: ProfileViewModel by viewModels {
                ProfileViewModelFactory(AccountServiceImpl())
            }

            val dataStoreViewModel : DataStoreViewModel by viewModels {
                DataStoreViewModelFactory(applicationContext.dataStore)
            }

            val mediaViewModel: MediaViewModel by viewModels {
                MediaViewModelFactory(
                    mediaService = MediaServiceImpl(),
                    accountService = AccountServiceImpl()
                )
            }

            val NotesTable by lazy {
                Room.databaseBuilder(applicationContext, NoteDatabase::class.java, "notes.db").build()
            }

            val noteViewModel: NoteViewModel by viewModels<NoteViewModel> {
                NoteViewModelFactory(
                    noteDao = NotesTable.noteDao,
                    mediaDao = NotesTable.mediaDao
                )
            }

            val dynamicColorState by dataStoreViewModel.readThemeSettings("dynamic_color").collectAsState(initial = false)
            val darkThemeState by dataStoreViewModel.readThemeSettings("dark_scheme").collectAsState(initial = isSystemInDarkTheme())

            SpektarTheme(
                dynamicColor = dynamicColorState,
                darkTheme = darkThemeState
            ) {
                SpektarNavigation(
                    mediaViewModel,
                    dataStoreViewModel,
                    noteViewModel,
                    profileViewModel,
                )
            }
        }
    }
}