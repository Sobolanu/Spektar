package com.example.spektar.models.repositories

import com.example.spektar.models.SpecificMedia
import com.example.spektar.supabase
import io.github.jan.supabase.postgrest.from

class MovieRepository() {
    suspend fun getAllMovies(): List<SpecificMedia> {
        return supabase.from("movies").select().decodeList<SpecificMedia>()
    }
}