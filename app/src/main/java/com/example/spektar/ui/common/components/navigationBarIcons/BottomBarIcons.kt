package com.example.spektar.ui.common.components.navigationBarIcons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.spektar.R
import com.example.spektar.ui.common.components.NavigationItem

internal val bottomIcons = listOf(
    NavigationItem(
        title = R.string.home, // ind 0
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),

    NavigationItem(
        title = R.string.questionnaire, // ind 1
        selectedIcon = Icons.Filled.AddBox,
        unselectedIcon = Icons.Outlined.AddBox
    ),

    NavigationItem(
        title = R.string.settings, // ind 2
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )
)