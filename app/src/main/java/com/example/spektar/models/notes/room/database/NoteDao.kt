package com.example.spektar.models.notes.room.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Upsert
    suspend fun modifyNote(note : Note)
    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM note ORDER BY title")
    fun getAllNotes() : Flow<List<Note>>
}