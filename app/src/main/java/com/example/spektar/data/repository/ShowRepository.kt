package com.example.spektar.data.repository

import com.example.spektar.data.remote.SupabaseClientProvider
import com.example.spektar.data.model.SpecificMedia
import io.github.jan.supabase.postgrest.from

class ShowRepository {
    suspend fun getAllShows(): List<SpecificMedia> {
        return SupabaseClientProvider.client.from("shows").select().decodeList<SpecificMedia>()
    }
}