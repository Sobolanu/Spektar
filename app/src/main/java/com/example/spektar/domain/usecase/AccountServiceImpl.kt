package com.example.spektar.domain.usecase

import com.example.spektar.data.model.User
import com.example.spektar.data.remote.SupabaseClientProvider
import com.example.spektar.data.remote.SupabaseClientProvider.auth
import com.example.spektar.domain.model.AccountService
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.upload
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.io.File

// reminder to go to your supabase dashboard and redirect users when they confirm their link
// that is, your sign up composable should also tell you that you need to confirm your address

class AccountServiceImpl : AccountService {
    override suspend fun retrieveSession() : UserSession? {
        var session = auth.currentSessionOrNull()

        if(session != null) {
            return session
        }

        try {
            auth.refreshCurrentSession()

            session = auth.currentSessionOrNull()
        } catch (e: Exception) {
            Exception("Session doesn't exist", e)
            // would lead smwhere i guess
        }

        if(session != null) {
            return session
        }

        return null
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

    override suspend fun signIn(userEmail: String, userPassword: String) {
        auth.signInWith(Email) {
            email = userEmail
            password = userPassword
        }
    }

    override suspend fun retrieveUserDataWithId(id: String) : User {
        val data = SupabaseClientProvider.db
            .from("profiles")
            .select(columns = Columns.list(listOf("id", "username", "avatar_url"))) {
            filter {
                eq("id", id)
            }
        }
            .decodeSingle<User>()

        return data
    }

    override suspend fun signUp(username: String, userEmail: String, userPassword: String, avatar: File?) {
        requireNotNull(avatar)

        auth.signUpWith(Email) {
            email = userEmail
            password = userPassword
            data = buildJsonObject {
                put("username", username)
            }
        }

        val userId = auth.currentUserOrNull()?.id ?: error("User ID missing from session after sign up")
        SupabaseClientProvider.storage.from("avatars")
            .upload("$userId/${avatar.name}", avatar) { upsert = false }

        SupabaseClientProvider.db.from("profiles").update(
            mapOf(
                "avatar_url" to "$userId/${avatar.name}",
                "username" to username
            )
        ) {
            filter {eq ("id", userId)}
        }
    }

    override suspend fun signOut() {
        try {
            auth.signOut()
        } catch(e: Exception) {
            Exception("Something went wrong with signOut: ${e.message}")
        }
    }

    override suspend fun deleteAccount() {
        // SupabaseClientProvider.client.auth.admin.deleteUser()
    }
}