package com.example.spektar.data.remote

import com.example.spektar.data.model.User
import com.example.spektar.domain.model.services.AccountService
import com.example.spektar.ui.userAuthScreens.states.SignInRequest
import com.example.spektar.ui.userAuthScreens.states.SignUpRequest
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.upload
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.io.File

class AccountServiceImpl : AccountService {

    init {
        CoroutineScope(Dispatchers.Main).launch {
            SupabaseClientProvider.auth.sessionStatus.collect { status ->
                when (status) {
                    is SessionStatus.Authenticated -> {
                        _sessionFlow.value = status.session
                    }
                    is SessionStatus.NotAuthenticated -> {
                        _sessionFlow.value = null
                    }
                    else -> { } // empty cause idrk what else to do
                }
            }
        }

    }

    private val _sessionFlow = MutableStateFlow<UserSession?>(null)
    override val sessionFlow: StateFlow<UserSession?> get() =_sessionFlow

    override suspend fun retrieveSession(): UserSession? {
        return sessionFlow.value
    }

    override suspend fun retrieveUserId(): String {
        val session = retrieveSession()

        if (session != null) {
            try {
                val user = session.user

                if(user != null) {
                    return user.id
                }

            } catch (e : Exception) {
                Exception("User retrieval failed.", e)
                // try retrieving again?
            }
        }
        Exception("Retrieving session failed")
        return " "
    }

    override suspend fun signIn(state: SignInRequest) {
        SupabaseClientProvider.auth.signInWith(Email) {
            email = state.email
            password = state.password
        }
    }

    override suspend fun changePassword(userId: String, oldPassword: String, newPassword: String) {
        val requestData = mapOf( // i hope this works
            "current_plain_password" to oldPassword,
            "new_plain_password" to newPassword,
            "current_id" to userId
        )

        try {
            SupabaseClientProvider.client.postgrest.rpc("change_password", { requestData })
        } catch (e: Exception) {
            Exception("Something is wrong with the change password request. Exact error is: ${e.message}")
        }
    }

    override fun makeUseableStorageUrl(url: String) : String {
        return "https://rlyotyktmhyflfyljpmr.supabase.co/storage/v1/object/public/avatars/$url"
    }

    override suspend fun retrieveUserDataWithId(id: String) : User {
        val data = SupabaseClientProvider.db
            .from("profiles")
            .select(columns = Columns.Companion.list(listOf("id", "username", "avatar_url"))) {
            filter {
                eq("id", id)
            }
        }
            .decodeSingle<User>() // this is with updated user class

        return data
    }

    override suspend fun signUp(
        state: SignUpRequest
    ) {
        requireNotNull(state.avatar)

        SupabaseClientProvider.auth.signUpWith(Email) {
            email = state.email
            password = state.password
            data = buildJsonObject {
                put("username", state.username)
            }
        }

        val userId = SupabaseClientProvider.auth.currentUserOrNull()?.id ?: error("User ID missing from session after sign up")

        updateAvatar(userId, state.avatar!!, state.username)
        /*
        SupabaseClientProvider.storage.from("avatars")
            .upload("$userId/${state.avatar!!.name}", state.avatar!!) { upsert = true }
         */
        SupabaseClientProvider.db.from("profiles").update(
            mapOf(
                "avatar_url" to "${userId}/${state.avatar!!.name}",
                "username" to state.username
            )
        ) {
            filter { eq ("id", userId)}
        }
    }

    override suspend fun updateAvatar(userId: String, avatar: File, username: String) {
        SupabaseClientProvider.storage.from("avatars")
            .upload("${userId}/${avatar.name}", avatar) { upsert = true }

        SupabaseClientProvider.db.from("profiles").update(
            mapOf(
                "avatar_url" to "${userId}/${avatar.name}",
                "username" to username
            )
        ) {
            filter { eq ("id", userId)}
        }
    }

    override suspend fun signOut() {
        try {
            SupabaseClientProvider.auth.signOut()
        } catch(e: Exception) {
            Exception("Something went wrong with signOut: ${e.message}")
        }
    }

    override suspend fun deleteAccount() {
        SupabaseClientProvider.client.postgrest.rpc("auth_delete_self")
        signOut() // because jwt token would exist even after deleting the account
    }
}