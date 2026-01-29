package com.example.spektar.ui.settingsScreen.themeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.compose.SpektarTheme
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

            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SettingsSubScreenItem(
                text = stringResource(R.string.dynamic_color_title),
                switchState = state.dynamicColorEnabled,
                infoBoxText = stringResource(R.string.dynamic_color_info_box_text),
                showInfoBox = state.dynamicColorInfoBox,
                onInfoBoxClick = { onEvent(ThemeEvent.dynamicColorInfoBox(it)) },
                onCheckedChangeConfirmed = { newValue ->
                    onEvent(ThemeEvent.dynamicColorToggle(newValue))
                },
                previewState = { effect ->
                    onEvent(ThemeEvent.dynamicColorPreview(effect!!))
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
                },
                previewState = { effect ->
                    onEvent(ThemeEvent.darkModePreview(effect!!))
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
                },
                previewState = { effect ->
                    // nothing
                }
            )

            Spacer(
                modifier = Modifier.padding(vertical = 8.dp)
            )
            ThemePreviewScreen(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
                darkTheme = state.darkSchemePreview,
                dynColor = state.dynamicColorPreview
            )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemePreviewScreen(
    previewText: String,
    darkTheme: Boolean,
    dynColor: Boolean,
    size: Dp = 250.dp
) {
    SpektarTheme(
        darkTheme = darkTheme,
        dynamicColor = dynColor,
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(4.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "TopBar",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleSmall,
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = previewText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.padding(6.dp)
                    )
                }

                // Bottom bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.secondary)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "BottomBar",
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}