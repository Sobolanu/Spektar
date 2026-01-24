package com.example.spektar.ui.common.components

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

data class SettingsScreenCategory(
    @StringRes val titleRes: Int,
    val tabs: List<Triple<ImageVector, Int, Int>>,
)