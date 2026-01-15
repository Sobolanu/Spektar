package com.example.spektar.domain.model

import com.example.spektar.data.model.User
import io.github.jan.supabase.auth.user.UserSession
import java.io.File

interface AccountService {
    suspend fun retrieveUserId() : String
    suspend fun retrieveSession() : UserSession?
    suspend fun retrieveUserDataWithId(id : String) : User
    suspend fun signIn(userEmail: String, userPassword: String)
    suspend fun signUp(username: String, userEmail: String, userPassword: String, avatar : File?)
    suspend fun signOut()
    suspend fun deleteAccount()
}