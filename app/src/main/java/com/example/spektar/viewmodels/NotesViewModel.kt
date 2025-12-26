package com.example.spektar.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.example.spektar.models.notes.room.database.Note
import com.example.spektar.models.notes.room.database.NoteDao
import com.example.spektar.models.notes.room.database.NoteEvent
import com.example.spektar.models.notes.room.database.NoteState
import com.example.spektar.models.notes.room.database.SortType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModel(
    private val dao: NoteDao
) : ViewModel() {
    private val _sortType = MutableStateFlow(SortType.TITLE)
    private val _notes = _sortType
        .flatMapLatest { sortType ->
            when(sortType) {
                SortType.TITLE -> dao.getAllNotes()
                SortType.NOTE_CONTENT -> dao.getAllNotes() // placeholder right now
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(NoteState())
    val state = combine(_state, _sortType, _notes) { state, sortType, notes ->
        state.copy(
            notes = notes,
            sortType = sortType
        )
    }

    fun onEvent(event: NoteEvent) {
        when(event) {
            is NoteEvent.DeleteNote -> {
                viewModelScope.launch {
                    dao.deleteNote(event.note)
                }
            }

            is NoteEvent.ModifyNoteContent -> {
                _state.update {it.copy(
                    content = event.noteContent
                )}
            }

            is NoteEvent.SortNotes -> {
                _sortType.value = event.sortType
            }
        }
    }
}