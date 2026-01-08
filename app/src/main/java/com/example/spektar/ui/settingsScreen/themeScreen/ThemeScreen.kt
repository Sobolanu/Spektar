package com.example.spektar.ui.settingsScreen.themeScreen

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.spektar.ui.viewModels.DataStoreViewModel
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
    val dynamicColorEnabled by viewModel.read("dynamic_color").collectAsState(initial = false)
    val darkSchemeEnabled by viewModel.read("dark_scheme").collectAsState(initial = false)

    var dynamicColorInfoBoxEnabled by remember {mutableStateOf(false)}
    var lightDarkInfoBoxEnabled by remember {mutableStateOf(false)}

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { ThemeScreenTopBar() },
        bottomBar = { BottomBar(
            onBottomBarItemClick,
            selectedIcon
        ) },
        contentWindowInsets = WindowInsets(left = 8.dp)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            SettingsSubScreenItem(
                text = "Dynamic Color",
                switchState = dynamicColorEnabled,

                infoBoxText = "Dynamic color matches the color scheme of the app with your wallpaper colors." +
                "Dynamic color must be enabled in system settings. This does not override your selected light/dark scheme.",
                showInfoBox = dynamicColorInfoBoxEnabled,
                onInfoBoxClick = {
                    dynamicColorInfoBoxEnabled = it
                },
                onCheckedChange = { it ->
                    scope.launch {
                        viewModel.save("dynamic_color", it)
                    }
                },
            )

            SettingsSubScreenItem(
                text = "Light/Dark Scheme",
                switchState = darkSchemeEnabled,

                infoBoxText = "The app scheme is defaulted to the light scheme but on toggle this will " +
                        "switch on the app's dark theme.",
                showInfoBox = lightDarkInfoBoxEnabled,

                onInfoBoxClick = {
                    lightDarkInfoBoxEnabled = it
                },

                onCheckedChange = { it ->
                    scope.launch {
                        viewModel.save("dark_scheme", it)
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
                "App Theme"
            )
        },

        modifier = Modifier.padding(bottom = 16.dp)
    )
}