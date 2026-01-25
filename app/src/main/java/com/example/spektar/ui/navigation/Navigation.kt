package com.example.spektar.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.spektar.ui.common.ObserveAsEvents
import com.example.spektar.ui.common.SnackbarController
import com.example.spektar.ui.navigation.bottomBarNavigation.bottomBarNavigation
import com.example.spektar.ui.navigation.graphs.authGraph.AuthGraph
import com.example.spektar.ui.navigation.graphs.authGraph.UserLoginScreen
import com.example.spektar.ui.navigation.graphs.categoryGraph.CategoryGraph
import com.example.spektar.ui.navigation.graphs.categoryGraph.CategoryScreen
import com.example.spektar.ui.navigation.graphs.common.CommonGraph
import com.example.spektar.ui.navigation.graphs.settingsGraph.SettingsGraph
import kotlinx.coroutines.launch

/*
Navigation uses "modern" (used to be modern, however Navigation3 came out but i'm kinda crunched on time so
i don't have time to migrate to Navigation3) type-safe navigation, which is also quite easy to work with.
 */

@Composable
fun SpektarNavigation() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()
    ObserveAsEvents(
        flow = SnackbarController.events,
        snackbarHostState
    ) { event ->
        scope.launch {
            val result = snackbarHostState.showSnackbar(
                message = event.message,
                actionLabel = event.action?.name,
                duration = event.duration
            )

            SnackbarController.markDismissed()

            if (result == SnackbarResult.ActionPerformed) {
                event.action?.action?.invoke()
            }
        }
    }

    // serves to remove snackbar upon navigation to a different screen
    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, _, _ ->
            snackbarHostState.currentSnackbarData?.dismiss()
            SnackbarController.markDismissed()
        }
    }

    // used to specify the currently selected icon in the app's bottom bar
    var selectedIcon by remember { mutableIntStateOf(0) }

    // start will be UserLoginScreen(false)
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = UserLoginScreen(false),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(500)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(500)
                )
            }
        ) {
            AuthGraph(
                navController = navController,
            )

            CategoryGraph(
                navController = navController,
                onBottomBarClick = { index -> // where selectedIcon gets changed
                    if (selectedIcon != index) {
                        selectedIcon = index
                        bottomBarNavigation(navController, index)
                    }
                },
                selectedIconProvider = { selectedIcon }
            )

            SettingsGraph(
                navController = navController,
                onBottomBarClick = { index ->
                    if (selectedIcon != index) {
                        selectedIcon = index
                        bottomBarNavigation(navController, index)
                    }
                },
                selectedIconProvider = { selectedIcon }
            )

            CommonGraph(
                navController = navController,
                onBottomBarClick = { index ->
                    if (selectedIcon != index) {
                        selectedIcon = index
                        bottomBarNavigation(navController, index)
                    }
                },
                selectedIconProvider = { selectedIcon }
            )
        }
    }
}