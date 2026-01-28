package com.example.spektar.ui.settingsScreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.spektar.R
import com.example.spektar.ui.common.components.BottomBar
import com.example.spektar.ui.common.components.SettingsScreenCategory
import com.example.spektar.ui.common.lists.SettingsScreenCategories
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    onEvent: (SettingsEvent) -> Unit,
    navigateToScreen: (Int) -> Unit,
    onBottomBarItemClick: (Int) -> Unit,
    selectedIcon: Int,
    state: LanguageState
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val drawerIsOpen by remember { derivedStateOf { drawerState.isOpen } }

    Box(modifier = Modifier.fillMaxSize()) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                LanguageDrawerDemo(
                    onEvent = onEvent,
                    selectedLanguage = state.selectedLanguage,
                    onDismiss = { scope.launch { drawerState.close() } }
                )
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

                    // overlay inside of content box so it doesn't apply to drawer
                    AnimatedVisibility(
                        visible = state.phase != LanguageChangePhase.Idle,
                        enter = fadeIn(),
                        exit = fadeOut(),
                        modifier = Modifier.matchParentSize()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (state.phase == LanguageChangePhase.FadeIn || state.phase == LanguageChangePhase.Changing) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
    BackHandler(enabled = drawerIsOpen) {
        scope.launch { drawerState.close() }
    }

    LaunchedEffect(state.phase) {
        if (state.phase == LanguageChangePhase.Idle && drawerState.isOpen) {
            drawerState.close()
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
            text = stringResource(category.titleRes),
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.padding(vertical = 4.dp))

        category.tabs.forEach { index ->
            Button(
                onClick = { navigateToScreen(index.third) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onBackground
            ),
            ) {
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

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}

@Composable
fun LanguageDrawerDemo(
    onEvent: (SettingsEvent) -> Unit,
    selectedLanguage: String,
    onDismiss: () -> Unit
) {
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
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,

                modifier = Modifier.padding(4.dp)
            ) {
                Text(
                    text = label,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            if (code != selectedLanguage) {
                                onEvent(SettingsEvent.SelectLanguage(code))
                                onDismiss()
                            }
                        }
                        .padding(vertical = 8.dp)
                )

                if(selectedLanguage == code) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = stringResource(R.string.current_language),
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
        title = { Text(stringResource(R.string.settings_screen_title)) },
        modifier = Modifier.padding(bottom = 16.dp)
    )
}