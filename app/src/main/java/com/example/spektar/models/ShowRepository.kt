package com.example.spektar.models

import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.FirebaseFirestore

class ShowRepository(private val db: FirebaseFirestore) {
    suspend fun getAllShows(): List<SpecificMedia> {
        return try {
            val snapshot = db.collection("anime").get().await()
            snapshot.toObjects(SpecificMedia::class.java)
        } catch (_: Exception) {
            emptyList()
        }
    }
}
