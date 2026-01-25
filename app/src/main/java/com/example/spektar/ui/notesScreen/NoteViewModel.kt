package com.example.spektar.ui.notesScreen

import android.content.Context
import com.example.spektar.data.model.roomModels.MediaId
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.spektar.data.local.NoteDatabase
import com.example.spektar.data.model.roomModels.Note
import com.example.spektar.ui.notesScreen.NoteState
import com.example.spektar.ui.settingsScreen.SortType
import com.example.spektar.data.local.dao.MediaDao
import com.example.spektar.data.local.dao.NoteDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class NoteViewModel(
    private val noteDao: NoteDao,
    private val mediaDao: MediaDao
) : ViewModel() {
    private val _sortType = MutableStateFlow(SortType.CONTENT_LENGTH)
    private val _currentMediaId = MutableStateFlow<String?>(null)

    // notes flow depends on both sort type and current mediaId
    private val _notes = combine(_sortType, _currentMediaId) { sortType, mediaId ->
        Pair(sortType, mediaId)
    }.flatMapLatest { (sortType, mediaId) ->
        if (mediaId == null) {
            flowOf(emptyList())
        } else {
            when (sortType) {
                SortType.CONTENT_LENGTH -> noteDao.getNotesByMedia(mediaId)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(NoteState())
    val state = combine(_state, _sortType, _notes, _currentMediaId) { state, sortType, notes, mediaId ->
        state.copy(
            notes = notes,
            sortType = sortType,
            currentMediaId = mediaId
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteState())

    fun setMedia(mediaId: String) {
        _currentMediaId.value = mediaId
    }

    fun onEvent(event: NoteEvent, mediaId: String) : Boolean? {
        when(event) {
            is NoteEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteDao.deleteNote(event.note)
                }
            }

            is NoteEvent.SaveNote -> {
                val title = state.value.title
                val text = state.value.text

                val normalizedTitle = title.trim()
                if (normalizedTitle.isBlank() || state.value.notes.any { it.title.equals(normalizedTitle, ignoreCase = true) }) {
                    return true // cannot make a note if the title either is empty or already exists
                }

                val note = Note( // then set the mediaId column to the function parameter mediaId
                    mediaId = mediaId,
                    title = title,
                    text = text
                )

                viewModelScope.launch {
                    mediaDao.insertMedia(MediaId(mediaId))
                    noteDao.upsertNote(note)
                }

                _state.update { it.copy(
                    isAddingNote = false,
                        title = "",
                        text = ""
                ) }
            }

            is NoteEvent.SetText -> {
                if(event.noteId == null) {
                    _state.update {it.copy (text = event.text) }
                } else {
                    viewModelScope.launch {
                        val note = state.value.notes.find {it.id == event.noteId}
                        if(note != null) {
                            noteDao.upsertNote(note.copy(text = event.text))
                        }
                    }
                }
            }

            is NoteEvent.SetTitle -> {
                if(event.noteId == null) {
                    _state.update {it.copy (title = event.title) }
                } else {
                    viewModelScope.launch {
                        val note = state.value.notes.find {it.id == event.noteId}
                        if(note != null) {
                            noteDao.upsertNote(note.copy(title = event.title))
                        }
                    }
                }
            }

            is NoteEvent.SortNotes -> {
                _sortType.value = event.sortType
            }
        }

        return null
    }
}

@Suppress("UNCHECKED_CAST")
class NoteViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = Room.databaseBuilder(
            context.applicationContext,
            NoteDatabase::class.java,
            "notes.db"
        ).build()
        return NoteViewModel(db.noteDao, db.mediaDao) as T
    }
}