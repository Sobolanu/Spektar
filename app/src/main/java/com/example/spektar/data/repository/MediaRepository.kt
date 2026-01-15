package com.example.spektar.data.repository

import com.example.spektar.data.model.SpecificMedia
import com.example.spektar.data.remote.SupabaseClientProvider
import io.github.jan.supabase.postgrest.from

object MediaRepository {
    suspend fun getAllMediaInCategory(category: String): List<SpecificMedia> {
        return SupabaseClientProvider.db.from(category).select().decodeList<SpecificMedia>()
    }
}