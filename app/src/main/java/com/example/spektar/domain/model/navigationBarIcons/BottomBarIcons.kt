package com.example.spektar.domain.model.navigationBarIcons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import com.example.spektar.data.model.NavigationItem

internal val bottomIcons = listOf(
    NavigationItem(
        title = "Home", // ind 0
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),

    NavigationItem(
        title = "Questionnaire", // ind 1
        selectedIcon = Icons.Filled.AddBox,
        unselectedIcon = Icons.Outlined.AddBox
    ),

    NavigationItem(
        title = "Settings", // ind 2
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )
)