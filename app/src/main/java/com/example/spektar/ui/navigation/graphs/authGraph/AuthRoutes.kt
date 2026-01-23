package com.example.spektar.ui.navigation.graphs.authGraph

import kotlinx.serialization.Serializable

@Serializable data object Auth

@Serializable object UserRegistrationScreen
@Serializable data class UserLoginScreen(val showEmailPopUp: Boolean)