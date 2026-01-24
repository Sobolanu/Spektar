package com.example.spektar.domain.model.services

import com.example.spektar.data.model.User
import com.example.spektar.ui.userAuthScreens.states.SignInState
import com.example.spektar.ui.userAuthScreens.states.SignUpState
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.flow.StateFlow
import java.io.File

interface AccountService{
    val sessionFlow: StateFlow<UserSession?> // Makes the UserSession a flow for ease-of-use.
    suspend fun retrieveSession() : UserSession? // simply returns the current session
    suspend fun retrieveUserId() : String // returns the current user ID associated with the session
    suspend fun retrieveUserDataWithId(id : String) : User // obtains basic user information - name, avatar and id

    // user auth:
    suspend fun signIn(state: SignInState)
    suspend fun signUp(state: SignUpState)
    suspend fun signOut()
    suspend fun deleteAccount()

    // modification:
    suspend fun updateAvatar(userId: String, avatar: File, username: String)
    fun storageUrl(url: String) : String
    suspend fun changePassword(userId: String, oldPassword: String, newPassword: String)

    suspend fun resetUserSuggestions(userId: String)
}