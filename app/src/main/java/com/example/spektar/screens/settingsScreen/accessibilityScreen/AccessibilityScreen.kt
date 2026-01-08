package com.example.spektar.screens.settingsScreen.accessibilityScreen

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.spektar.screens.AppBars.BottomBar
import com.example.spektar.screens.settingsScreen.themeScreen.ScreenItem

@Composable
fun AccessibilityScreen(
    onBottomBarItemClick: ( Int ) -> Unit,
    selectedIcon: Int
) {
    var reduceMotionEnabled by remember { mutableStateOf(false) }
    var reduceMotionInfoBoxEnabled by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { AccessibilityScreenTopBar() },
        bottomBar = {
            BottomBar(
                onBottomBarItemClick,
                selectedIcon
            )
        },
        contentWindowInsets = WindowInsets(left = 8.dp)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            ScreenItem(
                text = "Reduce Motion",
                switchState = reduceMotionEnabled,
                infoBoxText = "When toggled, app animations will be removed in order to improve " +
                        "the user experience for individuals with motion sickness.",
                showInfoBox = reduceMotionInfoBoxEnabled,
                onInfoBoxClick = {
                    reduceMotionInfoBoxEnabled = it
                },
                onCheckedChange = {
                    reduceMotionEnabled = it
                }
            )

            // implement colorblind stuff i suppose?
            // and 3 contrast settings later
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AccessibilityScreenTopBar() {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),

        title = {
            Text(
                "App Theme"
            )
        },

        modifier = Modifier.padding(bottom = 16.dp)
    )
}