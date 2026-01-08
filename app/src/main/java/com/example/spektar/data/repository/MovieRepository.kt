package com.example.spektar.data.repository

import com.example.spektar.data.remote.SupabaseClientProvider
import com.example.spektar.domain.model.SpecificMedia
import io.github.jan.supabase.postgrest.from

class MovieRepository() {
    suspend fun getAllMovies(): List<SpecificMedia> {
        return SupabaseClientProvider.client.from("movies").select().decodeList<SpecificMedia>()
    }
}