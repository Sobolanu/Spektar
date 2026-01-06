package com.example.spektar.screens.settingsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.spektar.screens.AppBars.BottomBar

/*
TODO: implement rest of categories and their subscreens
 */

@Composable
fun SettingsScreen(
    navigateToScreen: ( Int ) -> Unit,
    onBottomBarItemClick: (Int) -> Unit,
) {
    Scaffold(
        bottomBar = { BottomBar(onBottomBarItemClick) },
        topBar = { SettingsScreenTopBar() },
        contentWindowInsets = WindowInsets(left = 8.dp)
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            SettingsScreenCategories.forEach{ index ->
                SettingsCategory(
                    category = index,
                    navigateToScreen
                )
            }
        }
    }
}

@Composable
fun SettingsCategory(
    category : SettingsScreenCategory,
    navigateToScreen: (Int) -> Unit
) {
    Column( // title of category & spacing between categories
        modifier = Modifier
            .padding(bottom = 8.dp)
    ) {
        Text(
            text = category.titleOfCategory,
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(
            modifier = Modifier.padding(vertical = 4.dp)
        )

        category.tabs.forEach { index ->
            Button(
                /* figure this out later
                    colors = ButtonColors(
                        containerColor = ,
                        contentColor = ,
                        disabledContainerColor = ,
                        disabledContentColor =
                    ),
                 */
                onClick = { navigateToScreen(index.third) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                ) {
                    Icon(
                        imageVector = index.first, // placeholder for now
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    Text(
                        text = index.second,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SettingsScreenTopBar() {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),

        title = {
            Text(
                "Settings"
            )
        },

        modifier = Modifier.padding(bottom = 16.dp)
    )
}