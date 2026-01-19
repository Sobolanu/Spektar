package com.example.spektar.domain.model

import com.example.spektar.data.model.User
import com.example.spektar.ui.viewModels.states.UserSignInData
import com.example.spektar.ui.viewModels.states.UserSignUpData
import io.github.jan.supabase.auth.user.UserSession

interface AccountService {
    suspend fun retrieveUserId() : String
    suspend fun retrieveSession() : UserSession?
    suspend fun retrieveUserDataWithId(id : String) : User
    suspend fun signIn(state: UserSignInData)
    suspend fun signUp(state: UserSignUpData)
    suspend fun signOut()
    suspend fun deleteAccount()
}