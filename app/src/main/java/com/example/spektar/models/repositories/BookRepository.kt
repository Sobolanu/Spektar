package com.example.spektar.models.repositories

import com.example.spektar.models.SpecificMedia
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class BookRepository(private val db: FirebaseFirestore) {
    suspend fun getAllBooks(): List<SpecificMedia> {
        return try {
            val snapshot = db.collection("books").get().await()
            snapshot.toObjects(SpecificMedia::class.java)
        } catch (_: Exception) {
            emptyList()
        }
    }
}