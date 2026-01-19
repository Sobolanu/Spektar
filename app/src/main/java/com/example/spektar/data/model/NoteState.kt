package com.example.spektar.data.model

import com.example.spektar.data.model.roomModels.Note
import com.example.spektar.domain.model.SortType

data class NoteState(
    // all notes for a media
    val notes: List<Note> = emptyList(),
    val currentMediaId: String? = null,
    // sortType for all notes
    val sortType: SortType = SortType.CONTENT_LENGTH,

    // note details:
    val title: String = "",
    val text: String = "",
    // val textStyle : TextStyle = TextStyle(),

    // used for dialogs and stuff?
    val isAddingNote: Boolean = false
)