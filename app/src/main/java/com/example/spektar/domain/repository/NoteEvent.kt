package com.example.spektar.domain.repository

import androidx.compose.ui.text.TextStyle
import com.example.spektar.data.model.Note
import com.example.spektar.domain.model.SortType

sealed interface NoteEvent {
    object SaveNote: NoteEvent
    data class SetTitle(val title: String) : NoteEvent
    data class SetText(val text: String) : NoteEvent
    // data class SetTextStyle(val TextStyle: TextStyle) : NoteEvent
    data class SortNotes(val sortType : SortType) : NoteEvent
    data class DeleteNote(val note: Note) : NoteEvent
}