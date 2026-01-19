package com.example.spektar.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.spektar.data.model.MediaId
import com.example.spektar.data.model.Note
import com.example.spektar.domain.repository.MediaDao
import com.example.spektar.domain.repository.NoteDao

@Database(
    entities = [Note::class, MediaId::class],
    version = 1, // default for now
)

abstract class NoteDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao
    abstract val mediaDao: MediaDao
}