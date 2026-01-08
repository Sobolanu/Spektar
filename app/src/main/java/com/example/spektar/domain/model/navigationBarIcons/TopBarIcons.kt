package com.example.spektar.domain.model.navigationBarIcons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.AccountCircle
import com.example.spektar.domain.model.NavigationItem

internal val topProfileIcon = NavigationItem(
    title = "Profile",
    selectedIcon = Icons.Filled.AccountCircle,
    unselectedIcon = Icons.Outlined.AccountCircle
)

internal val topBackArrowIcon = NavigationItem(
    title = "Back arrow",
    selectedIcon = Icons.AutoMirrored.Filled.ArrowBack,
    unselectedIcon = Icons.AutoMirrored.Outlined.ArrowBack
)