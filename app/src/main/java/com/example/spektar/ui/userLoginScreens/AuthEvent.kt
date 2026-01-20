package com.example.spektar.ui.userLoginScreens

import com.example.spektar.ui.viewModels.states.SignInRequest
import com.example.spektar.ui.viewModels.states.SignUpRequest
import java.io.File

sealed interface AuthEvent {
    data class SetUsername(val newUsername: String) : AuthEvent
    data class SetEmail(val newEmail: String) : AuthEvent
    data class SetPassword(val newPassword: String) : AuthEvent

    data class SignIn(val userData: SignInRequest) : AuthEvent

    // SIGN-UP EXCLUSIVE:
    data class SetAvatar(val avatar: File?) : AuthEvent
    data class SignUp(val userData: SignUpRequest) : AuthEvent
}