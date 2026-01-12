package com.example.spektar.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

data class SettingsScreenCategory(
    val titleOfCategory: String,
    val tabs: List<Triple<ImageVector, String, Int>>,
)