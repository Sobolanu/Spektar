package com.example.spektar.ui.navigation.graphs.settingsGraph

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.spektar.data.local.DataStore.dataStore
import com.example.spektar.ui.HomeScreen
import com.example.spektar.ui.common.AppLocaleManager
import com.example.spektar.ui.navigation.graphs.common.AppErrorScreen
import com.example.spektar.ui.navigation.graphs.common.ProfileScreen
import com.example.spektar.ui.navigation.utils.safeNavigate
import com.example.spektar.ui.settingsScreen.Access
import com.example.spektar.ui.settingsScreen.SettingsScreen
import com.example.spektar.ui.settingsScreen.SettingsViewModel
import com.example.spektar.ui.settingsScreen.SettingsViewModelFactory
import com.example.spektar.ui.settingsScreen.themeScreen.ThemeScreen
import com.example.spektar.ui.settingsScreen.themeScreen.ThemeViewModel
import com.example.spektar.ui.settingsScreen.themeScreen.ThemeViewModelFactory

fun NavGraphBuilder.SettingsGraph(
    navController : NavController,
    selectedIconProvider: () -> Int,
    onBottomBarClick: (Int) -> Unit
) {
    navigation<Settings>(startDestination = SettingsScreen) {
        composable<SettingsScreen> { backStackEntry ->
            val context = LocalContext.current
            val settingsViewModel : SettingsViewModel = viewModel<SettingsViewModel> (
                viewModelStoreOwner = backStackEntry,
                factory = SettingsViewModelFactory(AppLocaleManager(context))
            )

            val state = settingsViewModel.settingState.collectAsStateWithLifecycle()

            SettingsScreen(
                navigateToScreen = { id ->
                    navController.safeNavigate(
                        when (id) {
                            Access.THEME_SCREEN.ordinal -> ThemeScreen
                            Access.PROFILE_SETTINGS_SCREEN.ordinal -> ProfileScreen
                            Access.HELP_SUPPORT_SCREEN.ordinal -> HelpSupportScreen
                            else -> { AppErrorScreen("problem") }
                        }
                    )
                },

                onBottomBarItemClick = onBottomBarClick,
                selectedIcon = selectedIconProvider(),
                onEvent = { event ->
                    settingsViewModel.onEvent(event)
                },
                state = state.value
            )
        }

        composable<ThemeScreen> { backStackEntry ->
            val context = LocalContext.current
            val themeViewModel : ThemeViewModel = viewModel<ThemeViewModel> (
                viewModelStoreOwner = backStackEntry,
                factory = ThemeViewModelFactory(context.dataStore)
            )

            val state = themeViewModel.uiState.collectAsStateWithLifecycle()
            ThemeScreen(
                onBottomBarItemClick = onBottomBarClick,
                selectedIcon = selectedIconProvider(),
                onEvent = { event ->
                    themeViewModel.onEvent(event)
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