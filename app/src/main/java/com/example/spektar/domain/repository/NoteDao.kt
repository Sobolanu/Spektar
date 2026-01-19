package com.example.spektar.domain.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.spektar.data.model.MediaId
import com.example.spektar.data.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Upsert // updates if exists, inserts if doesn't
    suspend fun upsertNote(note: Note)

    @Upsert
    suspend fun insertMedia(media: MediaId)
    @Delete
    suspend fun deleteNote(note: Note)

    // will simply sort by longest "text" parameter in the Note data class
    // is default sort type by now but i guess your default would actually be "last accessed" or sm
    // table called "notes", refer to note.kt to find it
    @Query("SELECT * FROM notes ORDER BY LENGTH(text) DESC") // select * from note may have to be modified because "note" is not my db's name.
    fun getNotesOrderedByLongestContent(): Flow<List<Note>>

    @Transaction
    @Query("SELECT * FROM notes WHERE mediaId = :mediaId")
    fun getNotesByMedia(mediaId : String): Flow<List<Note>>
}

// fun getNotesOrderedByName(): Flow<List<Note>>