package com.example.spektar.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.spektar.data.local.dao.ArchiveDao
import com.example.spektar.data.model.roomModels.Media

@Database(
    entities = [Media::class],
    version = 1
)

abstract class ArchiveDatabase : RoomDatabase() {
    abstract val archiveDao : ArchiveDao
}