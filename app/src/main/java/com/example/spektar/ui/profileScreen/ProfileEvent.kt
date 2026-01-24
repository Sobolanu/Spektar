package com.example.spektar.ui.profileScreen

import java.io.File

sealed interface ProfileEvent {
    data class updateAvatar(val userId: String, val newAvatar: File, val username : String) : ProfileEvent
    data class updateUsername(val newUsername: String) : ProfileEvent
    object resetUserSuggestions : ProfileEvent
    object resetPassword : ProfileEvent // in case user doesn't know password, send confo via auth and ask for new password?
    object signOut : ProfileEvent
    object deleteAccount: ProfileEvent
}