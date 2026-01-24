package com.example.spektar.ui.settingsScreen.themeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.spektar.R
import com.example.spektar.ui.common.components.BottomBar
import com.example.spektar.ui.common.components.SettingsSubScreenItem

@Composable
fun ThemeScreen(
    onBottomBarItemClick: ( Int ) -> Unit,
    onEvent: (ThemeEvent) -> Unit,
    selectedIcon: Int,
    state: ThemeUiState
) {
    Scaffold(
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
                switchState = state.dynamicColorEnabled,
                infoBoxText = stringResource(R.string.dynamic_color_info_box_text),
                showInfoBox = state.dynamicColorInfoBox,
                onInfoBoxClick = { onEvent(ThemeEvent.dynamicColorInfoBox(it)) },
                onCheckedChangeConfirmed = { newValue ->
                    onEvent(ThemeEvent.dynamicColorToggle(newValue))
                }
            )

            SettingsSubScreenItem(
                text = stringResource(R.string.light_dark_scheme_title),
                switchState = state.darkSchemeEnabled,
                infoBoxText = stringResource(R.string.light_dark_scheme_info_box_text),
                showInfoBox = state.darkSchemeInfoBox,
                onInfoBoxClick = { onEvent(ThemeEvent.darkModeInfoBox(it)) },
                onCheckedChangeConfirmed = { newValue ->
                    onEvent(ThemeEvent.darkModeToggle(newValue))
                }
            )

            SettingsSubScreenItem (
                text = stringResource(R.string.reduce_motion_title),
                switchState = state.reduceMotionEnabled,
                infoBoxText = stringResource(R.string.reduce_motion_info_box_text),
                showInfoBox = state.reduceMotionInfoBox,
                onInfoBoxClick = { onEvent(ThemeEvent.reduceMotionToggleInfoBox(it)) },
                onCheckedChangeConfirmed = { newValue ->
                    onEvent(ThemeEvent.reduceMotionToggle(newValue))
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