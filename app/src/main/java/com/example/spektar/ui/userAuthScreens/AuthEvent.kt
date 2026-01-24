package com.example.spektar.ui.userAuthScreens

import com.example.spektar.ui.userAuthScreens.states.SignInState
import com.example.spektar.ui.userAuthScreens.states.SignUpState
import java.io.File

sealed interface AuthEvent {
    data class SetUsername(val newUsername: String) : AuthEvent
    data class SetEmail(val newEmail: String) : AuthEvent
    data class SetPassword(val newPassword: String) : AuthEvent

    data class SignIn(val userData: SignInState) : AuthEvent

    // SIGN-UP EXCLUSIVE:
    data class SetAvatar(val avatar: File?) : AuthEvent
    data class SignUp(val userData: SignUpState) : AuthEvent
}