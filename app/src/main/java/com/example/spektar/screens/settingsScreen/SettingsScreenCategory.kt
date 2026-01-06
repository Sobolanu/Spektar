package com.example.spektar.screens.settingsScreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Language
import androidx.compose.ui.graphics.vector.ImageVector


// TODO: remove useless screens from here and Navigation.kt
enum class navigateTo{
    THEME_SCREEN,
    ACCESSIBILITY_SCREEN,
    LANGUAGE_SCREEN, // useless, you'll get a list that pops up on top of settings screen
    PROFILE_SETTINGS_SCREEN,
    HELP_SUPPORT_SCREEN, // useless since this would lead to a website
    DONATE_SCREEN // useless
}

class SettingsScreenCategory(
    val titleOfCategory: String,
    val tabs: List<Triple<ImageVector, String, Int>>,
)

internal val customization = SettingsScreenCategory(
    titleOfCategory = "Customization",
    tabs = listOf(
        Triple(Icons.Filled.Brush, "Theme", navigateTo.THEME_SCREEN.ordinal),
        Triple(Icons.Filled.AccessibilityNew, "Accessibility", navigateTo.ACCESSIBILITY_SCREEN.ordinal),
        Triple(Icons.Filled.Language, "Language", navigateTo.LANGUAGE_SCREEN.ordinal),
    )
)

internal val account = SettingsScreenCategory(
    titleOfCategory = "Account",
    tabs = listOf(
        Triple(Icons.Filled.AccountCircle, "Profile Settings", navigateTo.PROFILE_SETTINGS_SCREEN.ordinal)
    )
)

internal val support = SettingsScreenCategory(
    titleOfCategory = "Support",
    tabs = listOf(
        Triple(Icons.AutoMirrored.Filled.Help, "Help & Support", navigateTo.HELP_SUPPORT_SCREEN.ordinal),
        Triple(Icons.Filled.Diamond, "Donate", navigateTo.DONATE_SCREEN.ordinal)
    )
)

internal val SettingsScreenCategories = listOf(customization, account, support)