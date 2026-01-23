package com.example.spektar.ui.settingsScreen.themeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.spektar.R
import com.example.spektar.ui.settingsScreen.DataStoreViewModel
import com.example.spektar.ui.common.components.BottomBar
import kotlinx.coroutines.launch
import com.example.spektar.ui.common.components.SettingsSubScreenItem

// make ViewModel for modifying app appearance,
// remember, the ViewModel should be responsible for loading and rendering stuff!!!

@Composable
fun ThemeScreen(
    onBottomBarItemClick: ( Int ) -> Unit,
    selectedIcon: Int,
    viewModel: DataStoreViewModel
) {
    // theme:
    val dynamicColorEnabled by viewModel.readThemeSettings("dynamic_color").collectAsState(initial = false)
    val darkSchemeEnabled by viewModel.readThemeSettings("dark_scheme").collectAsState(initial = false)
    // accessibility:
    val reduceMotionEnabled by viewModel.readThemeSettings("reduce_motion").collectAsState(initial = false)

    // infoboxes:
    var reduceMotionInfoBoxEnabled by remember { mutableStateOf(false) }
    var dynamicColorInfoBoxEnabled by remember {mutableStateOf(false)}
    var lightDarkInfoBoxEnabled by remember {mutableStateOf(false)}

    // snackbar thing:
    val snackbarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = {
            // can pass your custom snackbar here
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = { ThemeScreenTopBar() },
        bottomBar = { BottomBar(
            onBottomBarItemClick,
            selectedIcon,
        ) },
        contentWindowInsets = WindowInsets(left = 8.dp)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            SettingsSubScreenItem(
                text = stringResource(R.string.dynamic_color_title),
                switchState = dynamicColorEnabled,

                infoBoxText = stringResource(R.string.dynamic_color_info_box_text),
                showInfoBox = dynamicColorInfoBoxEnabled,
                onInfoBoxClick = {
                    dynamicColorInfoBoxEnabled = it
                },
                onCheckedChange = { newValue ->
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = "There are unsaved changes. Save now?",
                            actionLabel = "Save",
                            duration = SnackbarDuration.Indefinite
                        )
                        when(result) {
                            SnackbarResult.ActionPerformed -> {
                                viewModel.saveThemeSettings("dynamic_color", newValue)
                            }
                            SnackbarResult.Dismissed -> {
                                /* Handle snackbar dismissed */
                            }
                        }
                    }
                },
            )

            SettingsSubScreenItem(
                text = stringResource(R.string.light_dark_scheme_title),
                switchState = darkSchemeEnabled,

                infoBoxText = stringResource(R.string.light_dark_scheme_info_box_text),
                showInfoBox = lightDarkInfoBoxEnabled,

                onInfoBoxClick = {
                    lightDarkInfoBoxEnabled = it
                },

                onCheckedChange = {
                    scope.launch {
                        viewModel.saveThemeSettings("dark_scheme", it)
                    }
                }
            )

            SettingsSubScreenItem (
                text = stringResource(R.string.reduce_motion_title),
                switchState = reduceMotionEnabled,
                infoBoxText = stringResource(R.string.reduce_motion_info_box_text),
                showInfoBox = reduceMotionInfoBoxEnabled,
                onInfoBoxClick = {
                    reduceMotionInfoBoxEnabled = it
                },
                onCheckedChange = {
                    scope.launch {
                        viewModel.saveThemeSettings("reduce_motion", it)
                    }
                }
            )

            // call a composable that represents a screen that previews the color scheme specified
            // this should actually be the home screen
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ThemeScreenTopBar() {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),

        title = {
            Text(
                stringResource(R.string.theme_screen_top_bar_text)
            )
        },

        modifier = Modifier.padding(bottom = 16.dp)
    )
}