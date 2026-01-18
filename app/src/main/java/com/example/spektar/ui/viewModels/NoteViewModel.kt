package com.example.spektar.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.spektar.data.model.Note
import com.example.spektar.data.model.NoteState
import com.example.spektar.domain.model.SortType
import com.example.spektar.domain.repository.NoteDao
import com.example.spektar.domain.repository.NoteEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class NoteViewModel(
    private val dao: NoteDao
) : ViewModel() {
    private val _sortType = MutableStateFlow(SortType.CONTENT_LENGTH)
    private val _notes = _sortType
        .flatMapLatest { sortType ->
            when(sortType) {
                SortType.CONTENT_LENGTH -> dao.getNotesOrderedByLongestContent()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(NoteState())
    val state = combine(_state, _sortType, _notes) {state, sortType, notes ->
        state.copy(
            notes = notes,
            sortType = sortType
        ) // 5000ms pause because this is a flow that is observed by the ui
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteState())

    fun onEvent(event: NoteEvent) {
        when(event) {
            is NoteEvent.DeleteNote -> {
                viewModelScope.launch {
                    dao.deleteNote(event.note)
                }
            }

            is NoteEvent.SaveNote -> {
                val title = state.value.title
                val text = state.value.text

                if(title.isBlank() || text.isBlank()) {
                    return // just leave because no data for inserting
                }

                val note = Note(
                    title = title,
                    text = text
                )

                viewModelScope.launch {
                    dao.upsertNote(note)
                }

                _state.update {it.copy(
                    isAddingNote = false,
                    title = "",
                    text = ""
                ) }
            }

            is NoteEvent.SetText -> {
                _state.update{ it.copy(
                    text = event.text
                )}
            }

            is NoteEvent.SetTitle -> {
                _state.update{ it.copy(
                    title = event.title
                )}
            }

            is NoteEvent.SortNotes -> {
                _sortType.value = event.sortType
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class NoteViewModelFactory(
    private val dao: NoteDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            return NoteViewModel(
                dao
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}