package com.example.spektar.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.spektar.ui.navigation.bottomBarNavigation.bottomBarNavigation
import com.example.spektar.ui.navigation.graphs.AuthGraph
import com.example.spektar.ui.navigation.graphs.CategoryGraph
import com.example.spektar.ui.navigation.graphs.SettingsGraph
import com.example.spektar.ui.navigation.routes.UserLoginScreen
import com.example.spektar.ui.viewModels.DataStoreViewModel
import com.example.spektar.ui.viewModels.MediaViewModel
import com.example.spektar.ui.viewModels.SignInViewModel
import com.example.spektar.ui.viewModels.SignUpViewModel

/*
Navigation uses "modern" (used to be modern, however Navigation3 came out but i'm kinda crunched on time so
i don't have time to migrate to Navigation3) type-safe navigation, which is also quite easy to work with.
 */

@Composable
fun SpektarNavigation(
    mediaViewModel : MediaViewModel,
    signInViewModel : SignInViewModel,
    signUpViewModel : SignUpViewModel,
    dataStoreViewModel : DataStoreViewModel
) {
    val navController = rememberNavController()
    // used to specify the currently selected icon in the app's bottom bar
    var selectedIcon by remember { mutableIntStateOf(0) }

    NavHost(navController = navController, startDestination = UserLoginScreen) {

        AuthGraph(
            navController = navController,
            signInViewModel = signInViewModel,
            signUpViewModel = signUpViewModel
        )

        CategoryGraph(
            navController = navController,
            mediaViewModel = mediaViewModel,
            selectedIcon = selectedIcon,
            onBottomBarClick = {index ->
                selectedIcon = index
                bottomBarNavigation(navController, index)
            }
        )

        SettingsGraph(
            navController = navController,
            dataStoreViewModel = dataStoreViewModel,
            selectedIcon = selectedIcon,
            onBottomBarClick = {index ->
                selectedIcon = index
                bottomBarNavigation(navController, index)
            }
        )

        CategoryGraph(
            navController = navController,
            mediaViewModel = mediaViewModel,
            selectedIcon = selectedIcon,
            onBottomBarClick = {index ->
                selectedIcon = index
                bottomBarNavigation(navController, index)
            }
        )
    }
}