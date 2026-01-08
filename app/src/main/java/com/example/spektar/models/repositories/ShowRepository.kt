package com.example.spektar.models.repositories

import com.example.spektar.models.SpecificMedia
import com.example.spektar.supabase
import io.github.jan.supabase.postgrest.from

class ShowRepository() {
    suspend fun getAllShows(): List<SpecificMedia> {
        return supabase.from("shows").select().decodeList<SpecificMedia>()
    }
}