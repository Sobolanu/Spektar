package com.example.spektar.ui.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.spektar.domain.model.Access
import com.example.spektar.ui.HomeScreen
import com.example.spektar.ui.navigation.routes.AccessibilityScreen
import com.example.spektar.ui.navigation.routes.AppErrorScreen
import com.example.spektar.ui.navigation.routes.DonateScreen
import com.example.spektar.ui.navigation.routes.HelpSupportScreen
import com.example.spektar.ui.navigation.routes.ProfileSettingsScreen
import com.example.spektar.ui.navigation.routes.Settings
import com.example.spektar.ui.navigation.routes.SettingsScreen
import com.example.spektar.ui.navigation.routes.ThemeScreen
import com.example.spektar.ui.settingsScreen.SettingsScreen
import com.example.spektar.ui.settingsScreen.accessibilityScreen.AccessibilityScreen
import com.example.spektar.ui.settingsScreen.profileSettingsScreen.ProfileSettingsScreen
import com.example.spektar.ui.settingsScreen.themeScreen.ThemeScreen
import com.example.spektar.ui.viewModels.DataStoreViewModel

fun NavGraphBuilder.SettingsGraph(
    navController : NavController,
    dataStoreViewModel: DataStoreViewModel,
    selectedIconProvider: () -> Int,
    onBottomBarClick: (Int) -> Unit
) {
    navigation<Settings>(startDestination = SettingsScreen) { // nested graph responsible for everything on settings page
        composable<SettingsScreen> {
            SettingsScreen(
                navigateToScreen = { id ->
                    navController.navigate(
                        // placeholder until i sort out error messaging
                        when (id) {
                            Access.THEME_SCREEN.ordinal -> ThemeScreen
                            Access.ACCESSIBILITY_SCREEN.ordinal -> AccessibilityScreen
                            Access.PROFILE_SETTINGS_SCREEN.ordinal -> ProfileSettingsScreen
                            Access.HELP_SUPPORT_SCREEN.ordinal -> HelpSupportScreen
                            Access.DONATE_SCREEN.ordinal -> DonateScreen
                            else -> { AppErrorScreen } // implement error screen when you get around to it
                        }
                    ) {launchSingleTop = true}
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

        composable<AccessibilityScreen> {
            AccessibilityScreen(
                onBottomBarItemClick = onBottomBarClick,
                selectedIcon = selectedIconProvider(),
                viewModel = dataStoreViewModel
            )
        }

        composable<ProfileSettingsScreen> {
            ProfileSettingsScreen(
                onBottomBarItemClick = onBottomBarClick,
                selectedIcon = selectedIconProvider(),
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