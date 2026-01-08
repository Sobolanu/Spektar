package com.example.spektar.domain.usecase

import io.github.jan.supabase.auth.user.UserInfo

interface AccountService {
    val currentUser: UserInfo?
    val currentUserId: String?
    fun hasUser(): Boolean

    suspend fun retrieveUser() : UserInfo
    suspend fun signIn(userEmail: String, userPassword: String)
    suspend fun signUp(userEmail: String, userPassword: String)
    suspend fun signOut()
    suspend fun deleteAccount()
}