package com.example.spektar.ui.navigation.bottomBarNavigation

import androidx.navigation.NavController
import com.example.spektar.ui.navigation.graphs.categoryGraph.CategoryScreen
import com.example.spektar.ui.navigation.graphs.common.HomeScreen
import com.example.spektar.ui.navigation.graphs.settingsGraph.SettingsScreen

fun bottomBarNavigation(
    navController : NavController,
    index : Int
) {
    navController.navigate(when (index) {
        0 -> HomeScreen
        1 -> CategoryScreen
        2 -> SettingsScreen
        else -> SettingsScreen
    } ) {
        launchSingleTop = true
        restoreState = true
    }
}