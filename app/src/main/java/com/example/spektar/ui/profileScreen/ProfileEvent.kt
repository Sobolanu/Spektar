package com.example.spektar.ui.profileScreen

import java.io.File

sealed interface ProfileEvent {
    data class updateAvatar(val newAvatar: File) : ProfileEvent
    data class updateUsername(val newUsername: String) : ProfileEvent
    data class resetUserSuggestions(val resetSuggestions: Boolean) : ProfileEvent
    object resetPassword : ProfileEvent // in case user doesn't know password, send confo via auth and ask for new password?
    object signOut : ProfileEvent
    object deleteAccount: ProfileEvent
}