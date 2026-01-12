package com.example.spektar.ui.navigation.routes

import kotlinx.serialization.Serializable

// These are the routes that the navigation uses, defined as either objects or data classes depending on
// if the routes require data passed between them or not

@Serializable
data class MediaDetails(
    val indexOfCategory: Int,
    val indexOfMediaInsideCategory: Int,
)
@Serializable
data class AppErrorScreen (
    val errorMessage: String
)

@Serializable // object UserLoginScreen
data class UserLoginScreen (
    val showEmailPopUp : Boolean
)
@Serializable object CategoryScreen
@Serializable object UserRegistrationScreen
@Serializable object SettingsScreen
@Serializable object ThemeScreen
@Serializable object Settings
@Serializable object AccessibilityScreen
@Serializable object ProfileSettingsScreen
@Serializable object HelpSupportScreen
@Serializable object DonateScreen