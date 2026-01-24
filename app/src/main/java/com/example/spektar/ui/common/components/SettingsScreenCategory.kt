package com.example.spektar.ui.common.components

import androidx.compose.ui.graphics.vector.ImageVector

data class SettingsScreenCategory(
    val titleRes: Int, // int instead of string because stringResource
    val tabs: List<Triple<ImageVector, Int, Int>>,
)