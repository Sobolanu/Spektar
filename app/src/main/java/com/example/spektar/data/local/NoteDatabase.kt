package com.example.spektar.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.spektar.data.model.Note
import com.example.spektar.domain.repository.NoteDao

@Database(
    entities = [Note::class],
    version = 1 // default for now
)

abstract class NoteDatabase : RoomDatabase() {
    abstract val dao: NoteDao
}