package com.example.spektar.ui.settingsScreen

/*
TODO: implement rest of categories and their subscreens

this screen would need a ViewModel of some kind to survive config changes
remember, the ViewModel should be responsible for preparing the data for loading stuff!
 */

import android.app.Activity
import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.spektar.R
import com.example.spektar.ui.common.LocaleHelper
import com.example.spektar.ui.common.components.BottomBar
import com.example.spektar.ui.common.components.SettingsScreenCategory
import com.example.spektar.ui.common.lists.SettingsScreenCategories
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    navigateToScreen: (Int) -> Unit,
    onBottomBarItemClick: (Int) -> Unit,
    selectedIcon: Int,
    viewModel: SettingsViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            LanguageDrawerDemo(
                viewModel = viewModel,
                onDismiss = {
                scope.launch {
                    drawerState.close()
                }
            })
        }
    ) {
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
                                    scope.launch { drawerState.open() }
                                } else {
                                    navigateToScreen(target)
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    BackHandler(enabled = drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }
}


@Composable
fun SettingsCategory(
    category: SettingsScreenCategory,
    navigateToScreen: (Int) -> Unit,
) {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Text(
            text = stringResource(category.titleRes),
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
                        contentDescription = stringResource(index.second),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = stringResource(index.second),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = stringResource(index.second),
                    )
                }
            }
        }
    }
}

@Composable
fun LanguageDrawerDemo(
    viewModel : SettingsViewModel,
    onDismiss: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val languages = listOf(
        "en" to stringResource(R.string.english),
        "sr" to stringResource(R.string.serbian)
    )

    Column(
        modifier = Modifier
            .width(220.dp)
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            stringResource(R.string.languages),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 44.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        languages.forEach { (code, label) ->
            Text(
                text = label,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        scope.launch {
                            // change language here
                            viewModel.saveLanguagePreferences(code)
                        }
                        onDismiss()
                    }
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
        title = { Text(stringResource(R.string.settings_screen_title)) },
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

