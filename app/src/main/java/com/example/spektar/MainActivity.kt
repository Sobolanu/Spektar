@file:Suppress("unused")

package com.example.spektar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.compose.SpektarTheme
import com.example.spektar.models.repositories.BookRepository
import com.example.spektar.models.repositories.CategoryRepository
import com.example.spektar.models.repositories.GameRepository
import com.example.spektar.models.repositories.MovieRepository
import com.example.spektar.models.repositories.ShowRepository
import com.example.spektar.screens.mediaCategories.MediaViewModel
import com.example.spektar.screens.mediaCategories.MediaViewModelFactory
import com.example.spektar.screens.userScreens.SignInViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

object FirestoreManager {
    val db: FirebaseFirestore by lazy { Firebase.firestore }
}

/*
    TODO:
        - apparently ViewModels may/may not contain the bones for navigation so sort that shit out
        - clean up the app a bit cause damn this project is MESSY.
*/

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpektarTheme(
                dynamicColor = false
            ) {
                val mediaViewModel: MediaViewModel by viewModels {
                    MediaViewModelFactory(
                        CategoryRepository(),

                        ShowRepository(FirebaseFirestore.getInstance()),
                        BookRepository(FirebaseFirestore.getInstance()),
                        GameRepository(FirebaseFirestore.getInstance()),
                        MovieRepository(db = FirebaseFirestore.getInstance())
                    )
                }

                val signInViewModel: SignInViewModel by viewModels()

                SpektarNavigation(
                    mediaViewModel,
                    signInViewModel
                )
            }
        }
    }
}