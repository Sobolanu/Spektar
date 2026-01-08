package com.example.spektar.data.repository

import com.example.spektar.data.remote.SupabaseClientProvider
import com.example.spektar.domain.model.SpecificMedia
import io.github.jan.supabase.postgrest.from

class BookRepository() {
    suspend fun getAllBooks() : List<SpecificMedia> {
        return SupabaseClientProvider.client.from("books").select().decodeList<SpecificMedia>()
    }
}