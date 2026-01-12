package com.example.spektar.domain.usecase

import com.example.spektar.data.remote.SupabaseClientProvider
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.storage.upload
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.io.File

// reminder to go to your supabase dashboard and redirect users when they confirm their link
// that is, your sign up composable should also tell you that you need to confirm your address

class AccountServiceImpl : AccountService {

    override fun hasUser(): Boolean {
        return SupabaseClientProvider.auth.currentUserOrNull() != null
    }
    override val currentUser: UserInfo?
        get() = SupabaseClientProvider.auth.currentUserOrNull()

    override val currentUserId: String?
        get() = currentUser?.id

    override suspend fun retrieveUser(): UserInfo {
        return SupabaseClientProvider.auth.retrieveUserForCurrentSession(updateSession = true)
    }

    override suspend fun signIn(userEmail: String, userPassword: String) {
        SupabaseClientProvider.auth.signInWith(Email) {
            email = userEmail
            password = userPassword
        }
    }

    override suspend fun signUp(username: String, userEmail: String, userPassword: String, avatar: File?) {
        requireNotNull(avatar)

        val user = SupabaseClientProvider.auth.currentUserOrNull()
        val userId = user?.id ?: error("User ID missing from session")

        SupabaseClientProvider.storage.from("avatars")
            .upload("$userId/${avatar.name}", avatar) { upsert = false }

        SupabaseClientProvider.auth.signUpWith(Email) {
            email = userEmail
            password = userPassword
            data = buildJsonObject {
                put("username", username)
                put("avatar_url", "$userId/${avatar.name}")
            }
        }
    }

    override suspend fun signOut() {
        SupabaseClientProvider.client.auth.signOut()
    }

    override suspend fun deleteAccount() {
        SupabaseClientProvider.client.auth.admin.deleteUser(currentUserId!!)
    }
}