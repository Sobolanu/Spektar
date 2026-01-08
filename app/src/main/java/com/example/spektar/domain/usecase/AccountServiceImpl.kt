package com.example.spektar.domain.usecase

import com.example.spektar.data.remote.SupabaseClientProvider
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo

// reminder to go to your supabase dashboard and redirect users when they confirm their link
// that is, your sign up composable should also tell you that you need to confirm your address

class AccountServiceImpl : AccountService {
    override val currentUser: UserInfo? = SupabaseClientProvider.client.auth.currentUserOrNull()

    override val currentUserId: String? = currentUser?.id

    override fun hasUser(): Boolean {
        return SupabaseClientProvider.client.auth.currentUserOrNull() != null
    }

    override suspend fun retrieveUser(): UserInfo {
        return SupabaseClientProvider.client.auth.retrieveUserForCurrentSession(updateSession = true)
    }

    override suspend fun signIn(userEmail: String, userPassword: String) {
        SupabaseClientProvider.client.auth.signInWith(Email) {
            email = userEmail
            password = userPassword
        }
    }

    override suspend fun signUp(userEmail: String, userPassword: String) {
        SupabaseClientProvider.client.auth.signUpWith(Email) {
            email = userEmail
            password = userPassword
        }
    }

    override suspend fun signOut() {
        SupabaseClientProvider.client.auth.signOut()
    }

    override suspend fun deleteAccount() {
        SupabaseClientProvider.client.auth.admin.deleteUser(currentUserId!!)
    }
}