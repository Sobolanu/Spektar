package com.example.spektar.models.accountServices

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/*
     refer to firebase tutorial video, because as of now your app will lead to the login screen
     even if you have an account, forcing you to login AGAIN.

     well damn i'm migrating to supabase, have fun me
*/

class AccountServiceImpl : AccountService {

    override val currentUser: Flow<User?>
        get() = callbackFlow {
            val listener = FirebaseAuth.AuthStateListener { auth ->
                this.trySend(auth.currentUser?.let {User(it.uid)})
            }

            Firebase.auth.addAuthStateListener(listener)
            awaitClose {Firebase.auth.removeAuthStateListener {listener}}
        }

    override val currentUserId: String
        get() = Firebase.auth.currentUser?.uid.orEmpty()

    override fun hasUser(): Boolean {
        return Firebase.auth.currentUser != null
    }

    // i gotta learn more about asynchronous functions cause what the hell does .await() even do
    override suspend fun signIn(email: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun signUp(email: String, password: String) {
        Firebase.auth.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun signOut() {
        Firebase.auth.signOut()
    }

    override suspend fun deleteAccount() {
        Firebase.auth.currentUser!!.delete()
    }
}