package com.example.spektar.domain.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.spektar.data.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Upsert // updates if exists, inserts if doesn't
    suspend fun upsertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    // will simply sort by longest "text" parameter in the Note data class
    // is default sort type by now but i guess your default would actually be "last accessed" or sm
    @Query("SELECT * FROM note ORDER BY LENGTH(text) DESC")
    fun getNotesOrderedByLongestContent(): Flow<List<Note>>
}

// fun getNotesOrderedByName(): Flow<List<Note>>