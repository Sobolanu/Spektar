package com.example.spektar.ui.navigation.graphs.common

import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.spektar.data.model.User
import com.example.spektar.ui.common.ErrorScreen
import com.example.spektar.ui.navigation.utils.navTypeOf
import com.example.spektar.ui.profileScreen.ProfileScreen
import com.example.spektar.ui.profileScreen.ProfileViewModel
import kotlin.reflect.typeOf

fun NavGraphBuilder.CommonGraph(
    navController : NavController,
    selectedIconProvider: () -> Int,
    onBottomBarClick: (Int) -> Unit
) {
    composable<ProfileScreen> {
        // integrate profileViewModel: ProfileViewModel,
        val state = User() // temporary

        ProfileScreen(
            onBottomBarItemClick = onBottomBarClick,
            selectedIcon = selectedIconProvider(),

            onEvent = { event ->
                // profileViewModel.onEvent((event)) // temporary
            },

            state = state // temporary
        )
    }

    composable<AppErrorScreen>(
        typeMap = mapOf(typeOf<AppErrorScreen>() to navTypeOf<AppErrorScreen>())
    ) { backStackEntry ->
        val args = backStackEntry.toRoute<AppErrorScreen>()
        ErrorScreen(args.errorMessage)
    }
}