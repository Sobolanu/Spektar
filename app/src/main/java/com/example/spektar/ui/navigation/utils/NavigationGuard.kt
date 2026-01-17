package com.example.spektar.ui.navigation.utils

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavController
import androidx.navigation.NavDestination

@Composable
fun rememberNavigationGuard(navController: NavController): NavigationGuard {
    var navigating by rememberSaveable { mutableStateOf(false) }

    // Reset navigating whenever the destination changes
    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, _: NavDestination, _ ->
            navigating = false
        }
    }

    return NavigationGuard(navController, navigating) { navigating = it }
}

class NavigationGuard(
    val navController: NavController,
    val navigating: Boolean,
    val setNavigating: (Boolean) -> Unit
) {
    inline fun <reified T : Any> safeNavigate(destination: T) {
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        val targetRoute = destination::class.qualifiedName

        if (!navigating && currentRoute != targetRoute) {
            setNavigating(true)
            navController.navigate(destination)
        }
    }
}
