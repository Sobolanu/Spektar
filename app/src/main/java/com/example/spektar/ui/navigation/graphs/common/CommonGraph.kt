package com.example.spektar.ui.navigation.graphs.common

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.spektar.data.remote.AccountServiceImpl
import com.example.spektar.ui.common.ErrorScreen
import com.example.spektar.ui.navigation.utils.navTypeOf
import com.example.spektar.ui.profileScreen.ProfileScreen
import com.example.spektar.ui.profileScreen.ProfileViewModel
import com.example.spektar.ui.profileScreen.ProfileViewModelFactory
import kotlin.reflect.typeOf

fun NavGraphBuilder.CommonGraph(
    navController : NavController,
    selectedIconProvider: () -> Int,
    onBottomBarClick: (Int) -> Unit
) {
    composable<ProfileScreen> {
        val profileViewModel: ProfileViewModel = viewModel<ProfileViewModel>(
            factory = ProfileViewModelFactory(AccountServiceImpl())
        )

        val state = profileViewModel.state.collectAsStateWithLifecycle()

        ProfileScreen(
            onBottomBarItemClick = onBottomBarClick,
            selectedIcon = selectedIconProvider(),

            onEvent = { event ->
                // profileViewModel.onEvent((event)) // temporary
            },

            state = state.value // temporary
        )
    }

    composable<AppErrorScreen>(
        typeMap = mapOf(typeOf<AppErrorScreen>() to navTypeOf<AppErrorScreen>())
    ) { backStackEntry ->
        val args = backStackEntry.toRoute<AppErrorScreen>()
        ErrorScreen(args.errorMessage)
    }
}