package com.example.spektar.screens.settingsScreen.themeScreen

import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.spektar.models.DataStoreViewModel
import com.example.spektar.screens.AppBars.BottomBar
import kotlinx.coroutines.launch

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
            ScreenItem(
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

            ScreenItem(
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

@Composable
fun infoBox( // add an animation that slowly opens this box
    text : String,
    modifier : Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
fun ScreenItem(
    text: String,
    switchState: Boolean,

    infoBoxText: String,
    showInfoBox: Boolean,

    onInfoBoxClick: (Boolean) -> Unit,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text)

        IconButton(
            modifier = Modifier
                .size(36.dp)
                .padding(bottom = 16.dp),
            onClick = { onInfoBoxClick(!showInfoBox) }
        ) {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = null
            )
        }

        Spacer(
            modifier = Modifier
                .padding(end = 12.dp)
                .weight(.1f)
        )

        Switch(
            checked = switchState,
            onCheckedChange = { newValue ->
                onCheckedChange(newValue)
            }
        )
    }

    if (showInfoBox) {
        infoBox(
            text = infoBoxText,
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
        )
    }
}
