package com.example.spektar

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Preview
@Composable
fun SpektarApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Hobbies) {
        composable<Home> { backStackEntry -> // placeholder for home screen right now
            // HomeScreen()
            Column(
            ) {

            }
        }

        composable<Hobbies> {
            HobbiesScreen(navController)
        }

        composable<Details> {
            var args = it.toRoute<Details>()
            DetailsScreen()
        }
    }
}

@Serializable
object Home
@Serializable
object Hobbies

@Serializable
object Details // turning this to a data class fucks it up, find good solution for
// being able to click on an image on the Hobbies screen to open a Details window.

@Serializable
object Settings

@Serializable
object Profile