package com.example.spektar.data.repository

import com.example.spektar.data.model.media.MediaPreview
import com.example.spektar.data.remote.SupabaseClientProvider
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.filter.FilterOperator

object MediaRepository {
    suspend fun getAllMediaInCategory(category: String): List<MediaPreview> {
        return SupabaseClientProvider.db.from(category)
            .select(Columns.list("id_uuid", "name", "imageUrl"))
            .decodeList<MediaPreview>() // will return a list of SpecificMedia, where only the id_uuid, name and imageUrl properties are non-null.
    }

    // top-to-bottom order of database applies when it should listen to your mediaIds order
    suspend fun EXPERIMENTALgetAllMediaInCategory(category: String, mediaIds: List<String>): List<MediaPreview> {
        val rows = SupabaseClientProvider.db.from(category)
            .select(Columns.list("id_uuid", "name", "imageUrl")) {
                filter {
                    filter("id_uuid", FilterOperator.IN, mediaIds)
                }
            }
            .decodeList<MediaPreview>()


        val rowMap = rows.associateBy { it.id_uuid }
        val ordered = mediaIds.mapNotNull { id -> rowMap[id] }

        return ordered
    }
}

