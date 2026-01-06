package com.example.spektar.models.repositories

import com.example.spektar.errorHandling.Error
import com.example.spektar.errorHandling.FetchError
import com.example.spektar.errorHandling.Result
import com.example.spektar.models.SpecificMedia
import com.example.spektar.supabase
import com.google.firebase.firestore.FirebaseFirestore
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.tasks.await

class BookRepository() {
    suspend fun getAllBooks() : List<SpecificMedia> {
        return supabase.from("books").select().decodeList<SpecificMedia>()
    }
}