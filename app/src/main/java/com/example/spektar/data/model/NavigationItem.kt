package com.example.spektar.data.model

import androidx.compose.ui.graphics.vector.ImageVector

internal data class NavigationItem(
    val title : String,
    val selectedIcon : ImageVector,
    val unselectedIcon : ImageVector
)