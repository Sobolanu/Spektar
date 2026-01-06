package com.example.spektar.models.repositories

import com.example.spektar.models.SpecificMedia
import com.example.spektar.supabase
import com.google.firebase.firestore.FirebaseFirestore
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.tasks.await

class ShowRepository() {
    suspend fun getAllShows(): List<SpecificMedia> {
        return supabase.from("shows").select().decodeList<SpecificMedia>()
    }
}