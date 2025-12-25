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
import com.example.spektar.viewmodels.MediaViewModel
import com.example.spektar.viewmodels.MediaViewModelFactory
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

object FirestoreManager {
    val db: FirebaseFirestore by lazy { Firebase.firestore }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpektarTheme(
                dynamicColor = false
            ) {
                val viewModel: MediaViewModel by viewModels {
                    MediaViewModelFactory(
                        CategoryRepository(),

                        ShowRepository(FirebaseFirestore.getInstance()),
                        BookRepository(FirebaseFirestore.getInstance()),
                        GameRepository(FirebaseFirestore.getInstance()),
                        MovieRepository(db = FirebaseFirestore.getInstance())
                    )
                }

                SpektarNavigation(viewModel)
            }
        }
    }
}