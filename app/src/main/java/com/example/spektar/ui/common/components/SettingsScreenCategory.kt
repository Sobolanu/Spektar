package com.example.spektar.ui.common.components

import androidx.compose.ui.graphics.vector.ImageVector

data class SettingsScreenCategory(
    val titleOfCategory: String,
    val tabs: List<Triple<ImageVector, String, Int>>,
)