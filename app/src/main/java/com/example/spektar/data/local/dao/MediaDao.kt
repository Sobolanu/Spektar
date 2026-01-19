package com.example.spektar.data.local.dao

import androidx.room.Dao
import androidx.room.Upsert
import com.example.spektar.data.model.roomModels.MediaId

@Dao
interface MediaDao {
    @Upsert
    suspend fun insertMedia(media: MediaId)
}