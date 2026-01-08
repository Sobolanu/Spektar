package com.example.spektar.models.accountServices

import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.Flow

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