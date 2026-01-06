package com.example.spektar.models.repositories

import com.example.spektar.models.SpecificMedia
import com.example.spektar.supabase
import com.google.firebase.firestore.FirebaseFirestore
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.tasks.await

class MovieRepository() {
    suspend fun getAllMovies(): List<SpecificMedia> {
        return supabase.from("movies").select().decodeList<SpecificMedia>()
    }
}