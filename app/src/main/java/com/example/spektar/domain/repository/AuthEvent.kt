package com.example.spektar.domain.repository

import com.example.spektar.data.model.viewModelStates.UserSignInData
import com.example.spektar.data.model.viewModelStates.UserSignUpData
import java.io.File

sealed interface AuthEvent {
    data class SetUsername(val newUsername: String) : AuthEvent
    data class SetEmail(val newEmail: String) : AuthEvent
    data class SetPassword(val newPassword: String) : AuthEvent

    data class SignIn(val userData: UserSignInData) : AuthEvent

    // SIGN-UP EXCLUSIVE:
    data class SetAvatar(val avatar: File?) : AuthEvent
    data class SignUp(val userData: UserSignUpData) : AuthEvent
}