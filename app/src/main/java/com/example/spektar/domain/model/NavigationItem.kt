package com.example.spektar.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

internal data class NavigationItem(
    val title : String,
    val selectedIcon : ImageVector,
    val unselectedIcon : ImageVector
)