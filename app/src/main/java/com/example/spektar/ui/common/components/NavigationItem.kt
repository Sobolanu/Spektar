package com.example.spektar.ui.common.components

import androidx.compose.ui.graphics.vector.ImageVector

internal data class NavigationItem(
    // is int instead of string because of resources
    val title : Int,
    val selectedIcon : ImageVector,
    val unselectedIcon : ImageVector
)