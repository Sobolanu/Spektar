package com.example.spektar.domain.usecase

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Language
import com.example.spektar.domain.model.Access
import com.example.spektar.domain.model.SettingsScreenCategory

internal val customization = SettingsScreenCategory(
    titleOfCategory = "Customization",
    tabs = listOf(
        Triple(Icons.Filled.Brush, "Theme", Access.THEME_SCREEN.ordinal),
        Triple(Icons.Filled.AccessibilityNew, "Accessibility", Access.ACCESSIBILITY_SCREEN.ordinal),
        Triple(
            Icons.Filled.Language,
            "Language",
            Access.LANGUAGE_PANE.ordinal
        ), // will open a pane on the side of the screen
        // for language changing
    )
)

internal val account = SettingsScreenCategory(
    titleOfCategory = "Account",
    tabs = listOf(
        Triple(Icons.Filled.AccountCircle, "Profile Settings", Access.PROFILE_SETTINGS_SCREEN.ordinal)
    )
)

internal val support = SettingsScreenCategory(
    titleOfCategory = "Support",
    tabs = listOf(
        Triple(Icons.AutoMirrored.Filled.Help, "Help & Support", Access.HELP_SUPPORT_SCREEN.ordinal),
        Triple(Icons.Filled.Diamond, "Donate", Access.HELP_SUPPORT_SCREEN.ordinal)
    )
)

internal val SettingsScreenCategories = listOf(customization, account, support)