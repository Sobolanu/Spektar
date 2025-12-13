package com.example.spektar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                            onImageClick = { navController.navigate(MediaDetails) },
                            viewModel = viewModel
                        )
                    }

                    composable<MediaDetails> {
                        MediaDetailsScreen(
                            onBackClick = { navController.popBackStack() }
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
object MediaDetails