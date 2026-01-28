package com.example.spektar.data.remote.authService

import arrow.core.Either
import arrow.core.raise.either
import com.example.spektar.data.model.User
import com.example.spektar.data.remote.SupabaseClientProvider
import com.example.spektar.domain.model.services.AccountService
import com.example.spektar.ui.userAuthScreens.states.SignInState
import com.example.spektar.ui.userAuthScreens.states.SignUpState
import io.github.jan.supabase.auth.exception.AuthErrorCode
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Count
import io.github.jan.supabase.storage.upload
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.io.File


class AccountServiceImpl : AccountService {

    // SESSION RELATED:

    private val _sessionFlow = MutableStateFlow<UserSession?>(null)
    override val sessionFlow: StateFlow<UserSession?> get() =_sessionFlow

    private val _signUpObserver = MutableStateFlow<Boolean?>(null)
    override val signUpObserver: StateFlow<Boolean?> get() = _signUpObserver

    init {
        CoroutineScope(Dispatchers.Main).launch {
            SupabaseClientProvider.auth.sessionStatus.collect { status ->
                when (status) {
                    is SessionStatus.Authenticated -> {
                        _sessionFlow.value = status.session
                        if(_signUpObserver.value == null) {
                            _signUpObserver.value = false // doesn't need to sign up
                        }
                    }

                    is SessionStatus.NotAuthenticated -> {
                        _sessionFlow.value = null
                        if(_signUpObserver.value == null) {
                            _signUpObserver.value = true // must sign up
                        }
                    }

                    else -> { }
                }
            }
        }
    }

    override suspend fun retrieveSession(): UserSession? {
        return sessionFlow.value
    }

    override suspend fun retrieveUserId(): Either<SessionFailure, String> =
        Either.catch {
            val session = retrieveSession()
            val user = requireNotNull(session).user
            requireNotNull(user).id
        }.mapLeft { e ->
            when (e) {
                is AuthRestException -> {
                   when(e.errorCode) {
                        AuthErrorCode.SessionNotFound -> SessionFailure.SessionNotFound
                        AuthErrorCode.SessionExpired -> SessionFailure.SessionExpired
                        AuthErrorCode.RequestTimeout -> SessionFailure.RequestTimeout

                        else -> SessionFailure.ErrorOccurred(e)
                    }
                }

                else -> SessionFailure.UnexpectedFailure
            }
        }

    override suspend fun retrieveUserDataWithId(id: String) : User {
        val data = SupabaseClientProvider.db
            .from("profiles")
            .select(columns = Columns.list(listOf("username", "avatar_url"))) {
                filter {
                    eq("id", id)
                }
            }
            .decodeSingle<User>() // this is with updated user class

        return data
    }

    // AUTH RELATED:

    override suspend fun signIn(state: SignInState) : Either<UserAuthFailure, Unit> = either {
        try {
            SupabaseClientProvider.auth.signInWith(Email) {
                email = state.email
                password = state.password
            }
            println("Sign in successful.")
            Either.Right(Unit)
        } catch (e: AuthRestException) {
            val failure = when(e.errorCode) {
                AuthErrorCode.UnexpectedFailure -> UserAuthFailure.UnexpectedFailure
                AuthErrorCode.ValidationFailed -> UserAuthFailure.ValidationFailed
                AuthErrorCode.EmailNotConfirmed -> UserAuthFailure.EmailNotConfirmed
                AuthErrorCode.InvalidCredentials -> UserAuthFailure.InvalidCredentials
                AuthErrorCode.EmailAddressNotAuthorized -> UserAuthFailure.EmailNotConfirmed
                AuthErrorCode.RequestTimeout -> UserAuthFailure.RequestTimeout
                AuthErrorCode.EmailAddressInvalid -> UserAuthFailure.EmailAddressInvalid
                AuthErrorCode.UserNotFound -> UserAuthFailure.UserNotFound
                else -> UserAuthFailure.ErrorOccurred(e)
            }

            raise(failure)
        }
    }

    override suspend fun signUp(state: SignUpState): Either<UserAuthFailure, Unit> = either {
        // 1) check username uniqueness (your existing code)
        try {
            val resp = SupabaseClientProvider.db.from("profiles")
                .select(columns = Columns.list(listOf("username"))) {
                    limit(1)
                    count(Count.EXACT)
                    filter { eq("username", state.username) }
                }
            val existingUserCount = resp.countOrNull()
            if (existingUserCount != null && existingUserCount > 0) {
                raise(UserAuthFailure.UsernameAlreadyExists)
            }
        } catch (e: RuntimeException) {
            raise(UserAuthFailure.UsernameAlreadyExists)
        } catch (e: Exception) {
            raise(UserAuthFailure.UnexpectedFailure)
        }

        // 2) create auth user
        try {
            SupabaseClientProvider.auth.signUpWith(Email) {
                email = state.email
                password = state.password
                data = buildJsonObject { put("username", state.username) }
            }
        } catch (e: AuthRestException) {
            val failure = when (e.errorCode) {
                AuthErrorCode.UnexpectedFailure -> UserAuthFailure.UnexpectedFailure
                AuthErrorCode.ValidationFailed -> UserAuthFailure.ValidationFailed
                AuthErrorCode.RequestTimeout -> UserAuthFailure.RequestTimeout
                AuthErrorCode.WeakPassword -> UserAuthFailure.WeakPassword
                AuthErrorCode.UserAlreadyExists -> UserAuthFailure.UserAlreadyExists
                AuthErrorCode.EmailExists -> UserAuthFailure.EmailExists
                else -> UserAuthFailure.ErrorOccurred(e)
            }
            raise(failure)
        }

        // 3) get user id
        val userIdResult = retrieveUserId()
        userIdResult.fold(
            ifLeft = { raise(UserAuthFailure.UnexpectedFailure) },
            ifRight = { userId ->
                // 4) upload avatar and update profile — await and check result
                val uploadResult = updateAvatar(userId, state.avatar!!, state.username)
                uploadResult.fold(
                    ifLeft = { raise(UserAuthFailure.UnexpectedFailure) },
                    ifRight = {
                        // 5) update profile row (if you still need to update again)
                        try {
                            SupabaseClientProvider.db.from("profiles").update(
                                mapOf(
                                    "avatar_url" to "${userId}/${state.avatar!!.name}",
                                    "username" to state.username
                                )
                            ) {
                                filter { eq("id", userId) }
                            }
                        } catch (e: Exception) {
                            raise(UserAuthFailure.UnexpectedFailure)
                        }
                    }
                )
            }
        )

        // 6) only now return success
        Either.Right(Unit)
    }


    override suspend fun signOut() {
        try {
            SupabaseClientProvider.auth.signOut()
        } catch(e: Exception) {
            Exception("Something went wrong with signOut: ${e.message}")
        }
    }

    override suspend fun deleteAccount() {
        try {
            SupabaseClientProvider.client.postgrest.rpc("auth_delete_self")
            signOut()
        } catch (e: Throwable) {
            println("Exception says: ${e.cause} \n")
            Exception("Something is wrong with the request to delete an account. Exact error is: \n ${e.message}")
        }
    }

    // USER MODIFICATION:

    override fun storageUrl(url: String) : String {
        return "https://rlyotyktmhyflfyljpmr.supabase.co/storage/v1/object/public/avatars/$url"
    }

    override suspend fun changePassword(userId: String, oldPassword: String, newPassword: String) {
        val requestData = mapOf(
            "current_plain_password" to oldPassword,
            "new_plain_password" to newPassword,
            "current_id" to userId
        )

        try {
            SupabaseClientProvider.client.postgrest.rpc("change_password", { requestData })
        } catch (e: Throwable) {
            println("Exception says: ${e.cause}\n")
            Exception("Something is wrong with the change password request. Exact error is: ${e.message}")
        }
    }

    override suspend fun updateAvatar(
        userId: String,
        avatar: File,
        username: String
    ): Either<DataUploadFailure, Unit> = either {
        try {
            SupabaseClientProvider.storage.from("avatars")
                .upload("${userId}/${avatar.name}", avatar) { upsert = true }

            // optional: verify upload response if API returns status
            SupabaseClientProvider.db.from("profiles").update(
                mapOf(
                    "avatar_url" to "${userId}/${avatar.name}",
                    "username" to username
                )
            ) {
                filter { eq("id", userId) }
            }

            Either.Right(Unit)
        } catch (e: Exception) {
            raise(DataUploadFailure.RequestTimeout) // map to your DataUploadFailure
        }
    }

    // have as backup just in case
    override suspend fun resetUserSuggestions(userId: String) : Either<DataUploadFailure, Unit> = either {
        try {
            val emptyEmbedding = List(47, { 0 })

            SupabaseClientProvider.db.from("profiles").update(
                mapOf("user_embedding" to "$emptyEmbedding")
            ) {
                filter { eq("id", userId) }
            }

            Either.Right(Unit)
        } catch (e: Exception) {
            Exception("Unknown Supabase DB error.")
        }
    }

    override suspend fun updateUserSuggestions(
        userId: String,
        embedding: List<Int>
    ): Either<DataUploadFailure, Unit> = either {
        try {
            SupabaseClientProvider.db.from("profiles").update(
                mapOf("user_embedding" to "$embedding")
            ) {
                filter { eq("id", userId) }
            }

            Either.Right(Unit)
        } catch (e: Exception) {
            Exception("Unknown Supabase DB error.")
        }
    }

}