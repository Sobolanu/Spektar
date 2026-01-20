package com.example.spektar.domain.model

import com.example.spektar.data.model.User
import com.example.spektar.ui.viewModels.states.SignInRequest
import com.example.spektar.ui.viewModels.states.SignUpRequest
import io.github.jan.supabase.auth.user.UserSession

interface AccountService {
    suspend fun retrieveUserId() : String
    suspend fun retrieveSession() : UserSession?
    suspend fun retrieveUserDataWithId(id : String) : User
    suspend fun signIn(state: SignInRequest)
    suspend fun signUp(state: SignUpRequest)

    fun makeUseableStorageUrl(url: String) : String

    suspend fun changePassword(userId: String, oldPassword: String, newPassword: String)
    suspend fun signOut()
    suspend fun deleteAccount()
}