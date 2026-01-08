package com.example.spektar.models.accountServices

import com.example.spektar.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo

/*
     refer to firebase tutorial video, because as of now your app will lead to the login screen
     even if you have an account, forcing you to login AGAIN.

     well damn i'm migrating to supabase, have fun me
*/

class AccountServiceImpl : AccountService {

    /*
    getCurrentSessionOrNull()?.user
     */
    override val currentUser: UserInfo? = auth.currentUserOrNull()

    override val currentUserId: String? = currentUser?.id

    override fun hasUser(): Boolean {
        return auth.currentUserOrNull() != null
    }

    override suspend fun retrieveUser(): UserInfo {
        return auth.retrieveUserForCurrentSession(updateSession = true)
    }

    override suspend fun signIn(userEmail: String, userPassword: String) {
        auth.signInWith(Email) {
            email = userEmail
            password = userPassword
        }
    }

    // reminder to go to your supabase dashboard and redirect users when they confirm their link
    // that is, your sign up composable should also tell you that you need to confirm your address
    override suspend fun signUp(userEmail: String, userPassword: String) {
        auth.signUpWith(Email) {
            email = userEmail
            password = userPassword
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun deleteAccount() {
        auth.admin.deleteUser(currentUserId!!)
    }
}