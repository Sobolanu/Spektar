package com.example.spektar.ui.userAuthScreens.states

data class SignInState(
    val email: String = "",
    val password: String = "",
    val snackBarText: String? = null,
    val signInFinished: Boolean = false
)