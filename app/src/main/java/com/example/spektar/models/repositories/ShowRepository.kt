package com.example.spektar.models.repositories

import com.example.spektar.models.SpecificMedia
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

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