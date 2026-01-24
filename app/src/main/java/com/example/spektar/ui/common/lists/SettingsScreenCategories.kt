package com.example.spektar.ui.common.lists

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Language
import com.example.spektar.R
import com.example.spektar.ui.common.components.SettingsScreenCategory
import com.example.spektar.ui.settingsScreen.Access

internal val customization = SettingsScreenCategory(
    titleRes = R.string.customization,
    tabs = listOf(
        Triple(Icons.Filled.Brush, R.string.theme_accessibility, Access.THEME_SCREEN.ordinal),
        Triple(
            Icons.Filled.Language,
            R.string.language,
            Access.LANGUAGE_PANE.ordinal // opens language pane in SettingsScreen
        ),
    )
)

internal val account = SettingsScreenCategory(
    titleRes = R.string.account,
    tabs = listOf(
        Triple(
            Icons.Filled.AccountCircle,
            R.string.profile_settings,
            Access.PROFILE_SETTINGS_SCREEN.ordinal
        )
    )
)

internal val support = SettingsScreenCategory(
    titleRes = R.string.support,
    tabs = listOf(
        Triple(
            Icons.AutoMirrored.Filled.Help,
            R.string.help_support,
            Access.HELP_SUPPORT_SCREEN.ordinal
        ),
        Triple(Icons.Filled.Diamond, R.string.donate, Access.HELP_SUPPORT_SCREEN.ordinal)
    )
)

internal val SettingsScreenCategories = listOf(customization, account, support)