package com.example.spektar.models

import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.FirebaseFirestore

/*
    Placeholder repository exclusive to anime.
*/

/*
    refer to https://firebase.google.com/docs/firestore/query-data/get-data?authuser=0 for information
*/

class AnimeRepository(private val db: FirebaseFirestore) {
    suspend fun getAllAnimes(): List<SpecificMedia> {
        return try {
            val snapshot = db.collection("anime").get().await()
            snapshot.toObjects(SpecificMedia::class.java)
        } catch (_: Exception) {
            emptyList()
        }
    }
}
