package com.example.spektar.ui.settingsScreen

import androidx.compose.foundation.background
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.spektar.R
import com.example.spektar.domain.model.Access
import com.example.spektar.domain.model.SettingsScreenCategory
import com.example.spektar.domain.usecase.SettingsScreenCategories
import com.example.spektar.ui.common.components.BottomBar

/*
TODO: implement rest of categories and their subscreens

this screen would need a ViewModel of some kind to survive config changes
remember, the ViewModel should be responsible for preparing the data for loading stuff!
 */

@Composable
fun SettingsScreen(
    navigateToScreen: ( Int ) -> Unit,
    onBottomBarItemClick: (Int) -> Unit,
    selectedIcon : Int
) {
    Scaffold(
        bottomBar = { BottomBar(
            selectedIcon = selectedIcon,
            onBottomBarItemClick = onBottomBarItemClick
        ) },
        topBar = { SettingsScreenTopBar() },
        contentWindowInsets = WindowInsets(left = 8.dp)
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            SettingsScreenCategories.forEach{ index ->
                SettingsCategory(
                    category = index,
                    navigateToScreen,
                )
            }
        }
    }
}

@Composable
fun SettingsCategory(
    category : SettingsScreenCategory,
    navigateToScreen: (Int) -> Unit,
) {
    Column( // title of category & spacing between categories
        modifier = Modifier.padding(bottom = 8.dp)
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

                onClick = {
                     if(index.third == Access.LANGUAGE_PANE.ordinal) {
                        // open language pane here
                     } else {
                        navigateToScreen(index.third)
                    }
                }
            ) {
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
