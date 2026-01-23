package com.example.spektar.domain.model.services

import com.example.spektar.data.model.User
import com.example.spektar.ui.userAuthScreens.states.SignInRequest
import com.example.spektar.ui.userAuthScreens.states.SignUpRequest
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.flow.StateFlow
import java.io.File

interface AccountService {
    val sessionFlow: StateFlow<UserSession?>
    suspend fun retrieveUserId() : String
    suspend fun retrieveSession() : UserSession?
    suspend fun retrieveUserDataWithId(id : String) : User
    suspend fun signIn(state: SignInRequest)
    suspend fun signUp(state: SignUpRequest)

    suspend fun updateAvatar(userId: String, avatar: File, username: String)

    fun makeUseableStorageUrl(url: String) : String

    suspend fun changePassword(userId: String, oldPassword: String, newPassword: String)
    suspend fun signOut()
    suspend fun deleteAccount()
}