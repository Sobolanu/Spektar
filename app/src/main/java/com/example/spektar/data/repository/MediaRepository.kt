package com.example.spektar.data.repository

import com.example.spektar.data.model.MediaPreview
import com.example.spektar.data.remote.SupabaseClientProvider
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
object MediaRepository {
    suspend fun getAllMediaInCategory(category: String): List<MediaPreview> {
            return SupabaseClientProvider.db.from(category)
            .select(Columns.list("id_uuid", "name", "imageUrl"))
            .decodeList<MediaPreview>() // will return a list of SpecificMedia, where only the id_uuid and imageUrl properties are non-null.
    }
}