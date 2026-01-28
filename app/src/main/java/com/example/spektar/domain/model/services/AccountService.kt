package com.example.spektar.domain.model.services

import arrow.core.Either
import com.example.spektar.data.model.User
import com.example.spektar.data.remote.authService.DataUploadFailure
import com.example.spektar.data.remote.authService.SessionFailure
import com.example.spektar.data.remote.authService.UserAuthFailure
import com.example.spektar.ui.userAuthScreens.states.SignInState
import com.example.spektar.ui.userAuthScreens.states.SignUpState
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.coroutines.flow.StateFlow
import java.io.File

interface AccountService{
    val sessionFlow: StateFlow<UserSession?> // Makes the UserSession a flow for ease-of-use.
    val signUpObserver: StateFlow<Boolean?>
    suspend fun retrieveSession() : UserSession? // simply returns the current session
    suspend fun retrieveUserId() : Either<SessionFailure, String> // returns the current user ID associated with the session
    suspend fun retrieveUserDataWithId(id : String) : User // obtains basic user information - name, avatar and id

    // user auth:
    suspend fun signIn(state: SignInState) : Either<UserAuthFailure, Unit>
    suspend fun signUp(state: SignUpState): Either<UserAuthFailure, Unit>
    suspend fun signOut()
    suspend fun deleteAccount()

    // modification:
    suspend fun updateAvatar(userId: String, avatar: File, username: String): Either<DataUploadFailure, Unit>
    fun storageUrl(url: String) : String
    suspend fun changePassword(userId: String, oldPassword: String, newPassword: String)
    suspend fun resetUserSuggestions(userId: String): Either<DataUploadFailure, Unit>

    suspend fun updateUserSuggestions(userId: String, embedding: List<Int>) : Either<DataUploadFailure, Unit>
}