package com.example.spektar.domain.repository

import androidx.room.Dao
import androidx.room.Upsert
import com.example.spektar.data.model.MediaId

@Dao
interface MediaDao {
    @Upsert
    suspend fun insertMedia(media: MediaId)
}