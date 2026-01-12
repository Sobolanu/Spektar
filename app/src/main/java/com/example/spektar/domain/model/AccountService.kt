package com.example.spektar.domain.usecase

import io.github.jan.supabase.auth.user.UserInfo
import java.io.File

interface AccountService {
    val currentUser: UserInfo?
    val currentUserId: String?
    fun hasUser(): Boolean

    suspend fun retrieveUser() : UserInfo
    suspend fun signIn(userEmail: String, userPassword: String)
    suspend fun signUp(username: String, userEmail: String, userPassword: String, avatar : File?)
    suspend fun signOut()
    suspend fun deleteAccount()
}