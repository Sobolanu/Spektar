package com.example.spektar.ui.navigation.bottomBarNavigation

import androidx.navigation.NavController
import com.example.spektar.ui.navigation.routes.Settings

fun bottomBarNavigation(
    navController : NavController,
    index : Int
) {
    navController.navigate(when (index) {
        0 -> Settings // all are Settings because i haven't made the rest yet
        1 -> Settings
        2 -> Settings
        else -> Settings
    } ) {
        launchSingleTop = true
        restoreState = true
    }
}