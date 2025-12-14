package com.example.spektar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.spektar.ui.theme.SpektarTheme
import com.example.spektar.screens.CategoryScreen
import com.example.spektar.screens.MediaDetailsScreen
import com.example.spektar.viewmodels.MediaViewModel
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpektarTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = CategoryScreen) {
                    composable<CategoryScreen> {
                        val viewModel : MediaViewModel = viewModel()
                        CategoryScreen(
                            onImageClick = { id ->
                                navController.navigate(MediaDetails(mediaID = id))
                            },
                            viewModel = viewModel
                        )
                    }

                    composable<MediaDetails> {
                        val viewModel : MediaViewModel = viewModel()
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