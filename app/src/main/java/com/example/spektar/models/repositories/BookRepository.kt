package com.example.spektar.models.repositories

import com.example.spektar.models.SpecificMedia
import com.example.spektar.supabase
import io.github.jan.supabase.postgrest.from

class BookRepository() {
    suspend fun getAllBooks() : List<SpecificMedia> {
        return supabase.from("books").select().decodeList<SpecificMedia>()
    }
}