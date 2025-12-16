package com.example.spektar

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.spektar.FirestoreManager.db
import com.example.spektar.models.AnimeRepository
import com.example.spektar.models.CategoryRepository
import com.example.spektar.models.SpecificMedia
import com.example.spektar.ui.theme.SpektarTheme
import com.example.spektar.screens.CategoryScreen
import com.example.spektar.screens.MediaDetailsScreen
import com.example.spektar.viewmodels.MediaViewModel
import com.example.spektar.viewmodels.MediaViewModelFactory
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.serialization.Serializable

object FirestoreManager {
    val db: FirebaseFirestore by lazy { Firebase.firestore }
}

/*
TODO: Implement Hilt for good dependency injections
 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpektarTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = CategoryScreen) {
                    composable<CategoryScreen> {
                        val viewModel: MediaViewModel by viewModels {
                            MediaViewModelFactory(
                                AnimeRepository(FirebaseFirestore.getInstance()),
                                CategoryRepository()
                            )
                        }

                        CategoryScreen(
                            onImageClick = { id ->
                                navController.navigate(MediaDetails(mediaID = id))
                            },
                            viewModel = viewModel
                        )
                    }

                    composable<MediaDetails> {
                        val viewModel: MediaViewModel by viewModels {
                            MediaViewModelFactory(
                                AnimeRepository(FirebaseFirestore.getInstance()),
                                CategoryRepository()
                            )
                        }

                        val args = it.toRoute<MediaDetails>()

                        MediaDetailsScreen(
                            onBackClick = { navController.popBackStack() },
                            mediaID = args.mediaID,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}

@Serializable
object CategoryScreen

@Serializable
data class MediaDetails(
    val mediaID : Int
)