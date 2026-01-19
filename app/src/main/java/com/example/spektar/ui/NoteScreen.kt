package com.example.spektar.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spektar.data.model.NoteState
import com.example.spektar.domain.repository.NoteEvent

// refer to https://www.youtube.com/watch?v=bOd3wO0uFr8&t=1453s for good UI design i suppose?

@Composable
fun NoteScreen(
    state: NoteState,
    mediaId: String,
    onEvent: (NoteEvent, String) -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TabRow(
                state,
                selectedTabIndex,
                onEvent,
                mediaId,
                onTabSelected = { selectedTabIndex = it }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = PaddingValues(4.dp),
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            if (selectedTabIndex == state.notes.size) {
                item { // "add note" tab
                    OutlinedTextField(
                        value = if(state.title == "") {
                            "Untitled $selectedTabIndex" // make this nice
                        } else {
                            state.title
                        },
                        onValueChange = { onEvent(NoteEvent.SetTitle(it), mediaId) },
                        textStyle = TextStyle(
                            fontSize = 24.sp
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            disabledBorderColor = Color.Transparent,
                            errorBorderColor = Color.Transparent
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )


                    Spacer(modifier = Modifier.padding(bottom = 16.dp))
                }
                item {
                    OutlinedTextField(
                        value = state.text,
                        onValueChange = { onEvent(NoteEvent.SetText(it), mediaId) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            disabledBorderColor = Color.Transparent,
                            errorBorderColor = Color.Transparent
                        ),
                        modifier = Modifier.fillParentMaxWidth().fillParentMaxHeight(0.8f)
                    )
                }
            } else {
                val note = state.notes[selectedTabIndex] // if a note exists, use this

                item {
                    OutlinedTextField(
                        value = note.title,
                        textStyle = TextStyle(
                            fontSize = 24.sp
                        ),
                        onValueChange = { onEvent(NoteEvent.SetTitle(it, note.id), mediaId) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            disabledBorderColor = Color.Transparent,
                            errorBorderColor = Color.Transparent
                        ),
                    )
                }
                item {
                    OutlinedTextField(
                        value = note.text,
                        onValueChange = { onEvent(NoteEvent.SetText(it, note.id), mediaId) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            disabledBorderColor = Color.Transparent,
                            errorBorderColor = Color.Transparent
                        ),
                    )
                }
            }
        }
    }
}

@Composable
fun TabRow(
    state: NoteState,
    selectedTabIndex: Int,
    onEvent: (NoteEvent, String) -> Unit,
    mediaId : String,
    onTabSelected: (Int) -> Unit
) {
    PrimaryScrollableTabRow(selectedTabIndex = selectedTabIndex, modifier = Modifier.padding(top = 24.dp)) {
        state.notes.forEachIndexed { index, note ->
            Tab(
                selected = (selectedTabIndex == index),
                onClick = { onTabSelected(index) },
                text = {
                    Row(
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = note.title, fontSize = 16.sp)
                        IconButton(onClick = { onEvent(NoteEvent.DeleteNote(note), mediaId) }) {
                            Icon(Icons.Filled.Close, contentDescription = null)
                        }
                    }
                }
            )
        }

        Tab(
            selected = (selectedTabIndex == state.notes.size),
            onClick = { onTabSelected(state.notes.size) },
            text = {
                Row(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Add note")
                    IconButton(onClick = { onEvent(NoteEvent.SaveNote, mediaId) }) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                    }
                }
            }
        )
    }
}
