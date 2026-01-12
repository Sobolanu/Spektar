package com.example.spektar.data.repository

import com.example.spektar.data.remote.SupabaseClientProvider
import com.example.spektar.data.model.SpecificMedia
import io.github.jan.supabase.postgrest.from

class GameRepository {
    suspend fun getAllGames(): List<SpecificMedia> {
        return SupabaseClientProvider.client.from("games").select().decodeList<SpecificMedia>()
    }
}