package com.example.spektar.models

import com.google.firebase.firestore.FirebaseFirestore

/*
    Placeholder repository exclusive to anime.
*/

/*
    refer to https://firebase.google.com/docs/firestore/query-data/get-data?authuser=0 for information

    - Implement AnimeRepository with kotlin coroutines!
    and you have to anyway because coroutines are used for anything asynchronous (like db.collection.get)
    because you call it and it waits before giving you the anime here
    it also gets rid of ugly viewmodel factories
*/


class AnimeRepository(
    private val db: FirebaseFirestore
) {
    fun getAllAnimes(onResult: (List<SpecificMedia>) -> Unit) {
        db.collection("anime")
            .get()
            .addOnSuccessListener { documentSnapshots ->
                val animes = documentSnapshots.toObjects(SpecificMedia::class.java)
                onResult(animes)
            }
            .addOnFailureListener { exception ->
                onResult(emptyList())
            }
    }
}