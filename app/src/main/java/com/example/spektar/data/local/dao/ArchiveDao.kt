package com.example.spektar.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.spektar.data.model.roomModels.Media
import kotlinx.coroutines.flow.Flow

@Dao
interface ArchiveDao {
    @Upsert
    suspend fun insertMediaToArchive(media: Media)

    @Delete
    suspend fun removeMediaFromArchive(media: Media)

    @Query("SELECT * FROM Media")
    fun getAllArchivedMedia(): Flow<List<Media>>

    @Query("UPDATE Media SET currentProgress = :newProgress WHERE id_uuid = :mediaId")
    suspend fun updateProgress(newProgress: Int, mediaId: String)
}