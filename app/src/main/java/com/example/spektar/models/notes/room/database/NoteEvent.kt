package com.example.spektar.models.notes.room.database

import com.example.spektar.models.notes.room.database.SortType

sealed interface NoteEvent{
    data class DeleteNote(val note : Note) : NoteEvent
    data class ModifyNoteContent(val noteContent: String): NoteEvent
    data class SortNotes(val sortType : SortType): NoteEvent
}