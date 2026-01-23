package com.example.spektar.ui.navigation.graphs.settingsGraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.spektar.ui.HomeScreen
import com.example.spektar.ui.navigation.graphs.common.AppErrorScreen
import com.example.spektar.ui.navigation.graphs.common.ProfileScreen
import com.example.spektar.ui.navigation.utils.safeNavigate
import com.example.spektar.ui.settingsScreen.Access
import com.example.spektar.ui.settingsScreen.DataStoreViewModel
import com.example.spektar.ui.settingsScreen.SettingsScreen
import com.example.spektar.ui.settingsScreen.themeScreen.ThemeScreen

fun NavGraphBuilder.SettingsGraph(
    navController : NavController,
    dataStoreViewModel: DataStoreViewModel,
    selectedIconProvider: () -> Int,
    onBottomBarClick: (Int) -> Unit
) {
    // clean up viewmodel stuff here
    navigation<Settings>(startDestination = SettingsScreen) {
        composable<SettingsScreen> {
            SettingsScreen(
                navigateToScreen = { id ->
                    navController.safeNavigate(
                        when (id) {
                            Access.THEME_SCREEN.ordinal -> ThemeScreen
                            Access.PROFILE_SETTINGS_SCREEN.ordinal -> ProfileScreen // this is profiles!!!
                            Access.HELP_SUPPORT_SCREEN.ordinal -> HelpSupportScreen
                            else -> { AppErrorScreen("problem") }
                        }
                    )
                },

                onBottomBarItemClick = onBottomBarClick,
                selectedIcon = selectedIconProvider(),
            )
        }

        composable<ThemeScreen> {
            ThemeScreen(
                onBottomBarItemClick = onBottomBarClick,
                selectedIcon = selectedIconProvider(),
                viewModel = dataStoreViewModel
            )
        }

        composable<HelpSupportScreen> {
            HomeScreen() // placeholder
        }

        composable<DonateScreen> {
           HomeScreen() // placeholder
        }
    }
}