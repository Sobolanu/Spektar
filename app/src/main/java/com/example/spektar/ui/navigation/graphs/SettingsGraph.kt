package com.example.spektar.ui.navigation.graphs

import androidx.compose.runtime.collectAsState
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
import com.example.spektar.ui.navigation.routes.ProfileScreen
import com.example.spektar.ui.navigation.routes.Settings
import com.example.spektar.ui.navigation.routes.SettingsScreen
import com.example.spektar.ui.navigation.routes.ThemeScreen
import com.example.spektar.ui.navigation.utils.safeNavigate
import com.example.spektar.ui.profileScreen.ProfileScreen
import com.example.spektar.ui.settingsScreen.SettingsScreen
import com.example.spektar.ui.settingsScreen.accessibilityScreen.AccessibilityScreen
import com.example.spektar.ui.settingsScreen.themeScreen.ThemeScreen
import com.example.spektar.ui.viewModels.DataStoreViewModel
import com.example.spektar.ui.viewModels.ProfileViewModel

fun NavGraphBuilder.SettingsGraph(
    navController : NavController,
    dataStoreViewModel: DataStoreViewModel,
    profileViewModel: ProfileViewModel,
    selectedIconProvider: () -> Int,
    onBottomBarClick: (Int) -> Unit
) {
    // clean up viewmodel stuff here
    navigation<Settings>(startDestination = SettingsScreen) { // nested graph responsible for everything on settings page
        composable<SettingsScreen> {
            SettingsScreen(
                navigateToScreen = { id ->
                    navController.safeNavigate(
                        // placeholder until i sort out error messaging
                        when (id) {
                            Access.THEME_SCREEN.ordinal -> ThemeScreen
                            Access.ACCESSIBILITY_SCREEN.ordinal -> AccessibilityScreen
                            Access.PROFILE_SETTINGS_SCREEN.ordinal -> ProfileScreen
                            Access.HELP_SUPPORT_SCREEN.ordinal -> HelpSupportScreen
                            Access.DONATE_SCREEN.ordinal -> DonateScreen
                            else -> { AppErrorScreen } // implement error screen when you get around to it
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

        composable<AccessibilityScreen> {
            AccessibilityScreen(
                onBottomBarItemClick = onBottomBarClick,
                selectedIcon = selectedIconProvider(),
                viewModel = dataStoreViewModel
            )
        }

        composable<ProfileScreen> {
            val state = profileViewModel.state.collectAsState()

            ProfileScreen(
                onBottomBarItemClick = onBottomBarClick,
                selectedIcon = selectedIconProvider(),

                onEvent = { event ->
                    profileViewModel.onEvent((event))
                },

                state = state.value
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