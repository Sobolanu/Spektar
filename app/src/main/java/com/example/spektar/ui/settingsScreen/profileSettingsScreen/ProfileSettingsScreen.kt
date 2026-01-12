package com.example.spektar.ui.settingsScreen.profileSettingsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.spektar.R
import com.example.spektar.ui.common.components.BottomBar

@Composable
fun ProfileSettingsScreen(
    onBottomBarItemClick: (Int) -> Unit,
    selectedIcon : Int,
) {
    Scaffold(
        bottomBar = {
            BottomBar(
                onBottomBarItemClick,
                selectedIcon,
            )
        },
        contentWindowInsets = WindowInsets(left = 8.dp)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "blabla"
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ProfileSettingsScreenTopBar() {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),

        title = {
            Text(
                stringResource(R.string.accessibility_screen_title)
            )
        },

        modifier = Modifier.padding(bottom = 16.dp)
    )
}