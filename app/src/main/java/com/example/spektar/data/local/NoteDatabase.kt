package com.example.spektar.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.spektar.data.local.dao.MediaDao
import com.example.spektar.data.local.dao.NoteDao
import com.example.spektar.data.model.roomModels.Note
import com.example.spektar.data.model.roomModels.MediaId

@Database(
    entities = [Note::class, MediaId::class],
    version = 1, // default for now
)

abstract class NoteDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao
    abstract val mediaDao: MediaDao
}