package com.example.spektar.models.notes.room.database

data class NoteState(
    val notes : List<Note> = emptyList(),
    val content: String = "",
    val isDeletingNote: Boolean = false,
    val sortType: SortType = SortType.TITLE
)
