package com.example.spektar.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.spektar.ui.navigation.bottomBarNavigation.bottomBarNavigation
import com.example.spektar.ui.navigation.graphs.authGraph.AuthGraph
import com.example.spektar.ui.navigation.graphs.authGraph.UserLoginScreen
import com.example.spektar.ui.navigation.graphs.categoryGraph.CategoryGraph
import com.example.spektar.ui.navigation.graphs.common.CommonGraph
import com.example.spektar.ui.navigation.graphs.settingsGraph.SettingsGraph
import com.example.spektar.ui.settingsScreen.DataStoreViewModel

/*
Navigation uses "modern" (used to be modern, however Navigation3 came out but i'm kinda crunched on time so
i don't have time to migrate to Navigation3) type-safe navigation, which is also quite easy to work with.
 */

@Composable
fun SpektarNavigation(
    dataStoreViewModel: DataStoreViewModel,
) {
    val navController = rememberNavController()
    // used to specify the currently selected icon in the app's bottom bar
    var selectedIcon by remember { mutableIntStateOf(0) }
    // start will be UserLoginScreen(false)
    NavHost(
        navController = navController,
        startDestination = UserLoginScreen(false),
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) },
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) },
        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(500)) },
        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(500)) }
    ) {
        AuthGraph(
            navController = navController,
        )

        CategoryGraph(
            navController = navController,
            onBottomBarClick = { index -> // where selectedIcon gets changed
                if(selectedIcon != index) {
                    selectedIcon = index
                    bottomBarNavigation(navController, index)
                }
            },
            selectedIconProvider = { selectedIcon }
        )

        SettingsGraph(
            navController = navController,
            dataStoreViewModel = dataStoreViewModel,
            onBottomBarClick = { index ->
                if(selectedIcon != index) {
                    selectedIcon = index
                    bottomBarNavigation(navController, index)
                }
            },
            selectedIconProvider = { selectedIcon }
        )

        CommonGraph(
            navController = navController,
            onBottomBarClick = { index ->
                if(selectedIcon != index) {
                    selectedIcon = index
                    bottomBarNavigation(navController, index)
                }
            },
            selectedIconProvider = { selectedIcon }
        )
    }
}