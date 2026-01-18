package com.example.spektar.data.model

import com.example.spektar.domain.model.SortType

data class NoteState(
    val notes: List<Note> = emptyList(),
    val title: String = "",
    val text: String = "",
    // val textStyle : TextStyle = TextStyle(),
    val isAddingNote: Boolean = false,
    val sortType: SortType = SortType.CONTENT_LENGTH
)