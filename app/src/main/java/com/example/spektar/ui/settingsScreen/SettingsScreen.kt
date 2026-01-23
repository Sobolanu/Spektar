package com.example.spektar.ui.settingsScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.spektar.R
import com.example.spektar.ui.settingsScreen.Access
import com.example.spektar.ui.common.components.SettingsScreenCategory
import com.example.spektar.ui.common.lists.SettingsScreenCategories
import com.example.spektar.ui.common.components.BottomBar

/*
TODO: implement rest of categories and their subscreens

this screen would need a ViewModel of some kind to survive config changes
remember, the ViewModel should be responsible for preparing the data for loading stuff!
 */

import androidx.activity.compose.BackHandler

@Composable
fun SettingsScreen(
    navigateToScreen: (Int) -> Unit,
    onBottomBarItemClick: (Int) -> Unit,
    selectedIcon: Int
) {
    var showLanguageDrawer by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomBar(
                selectedIcon = selectedIcon,
                onBottomBarItemClick = onBottomBarItemClick
            )
        },
        topBar = { SettingsScreenTopBar() },
        contentWindowInsets = WindowInsets(left = 8.dp)
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Main content
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
            ) {
                SettingsScreenCategories.forEach { index ->
                    SettingsCategory(
                        category = index,
                        navigateToScreen = { target ->
                            if (target == Access.LANGUAGE_PANE.ordinal) {
                                showLanguageDrawer = !showLanguageDrawer
                            } else {
                                navigateToScreen(target)
                            }
                        }
                    )
                }
            }

            // tab overlay
            AnimatedVisibility(
                visible = showLanguageDrawer,
                enter = slideInHorizontally(initialOffsetX = { -it }),
                exit = slideOutHorizontally(targetOffsetX = { -it }),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(paddingValues)
            ) {
                LanguageDrawerDemo(onDismiss = { showLanguageDrawer = false })
            }

            if (showLanguageDrawer) {
                BackHandler {
                    showLanguageDrawer = false
                }
            }
        }
    }
}


@Composable
fun SettingsCategory(
    category: SettingsScreenCategory,
    navigateToScreen: (Int) -> Unit,
) {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Text(
            text = category.titleOfCategory,
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.padding(vertical = 4.dp))

        category.tabs.forEach { index ->
            Button(onClick = { navigateToScreen(index.third) }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                ) {
                    Icon(
                        imageVector = index.first,
                        contentDescription = index.second,
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
fun LanguageDrawerDemo(onDismiss: () -> Unit) {
    val languages = listOf("English", "Serbian", "German")

    Column(
        modifier = Modifier
            .width(220.dp)
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text("Languages", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))
        languages.forEach { lang ->
            Text(
                text = lang,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDismiss() }
                    .padding(vertical = 8.dp)
            )
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
                stringResource(R.string.settings_screen_title)
            )
        },

        modifier = Modifier.padding(bottom = 16.dp)
    )
}
