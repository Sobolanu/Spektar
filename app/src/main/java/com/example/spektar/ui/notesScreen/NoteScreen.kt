package com.example.spektar.ui.notesScreen

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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spektar.R
import com.example.spektar.ui.common.SnackbarAction
import com.example.spektar.ui.common.SnackbarController
import com.example.spektar.ui.common.SnackbarEvent
import com.example.spektar.ui.notesScreen.NoteState
import com.example.spektar.ui.notesScreen.NoteEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// refer to https://www.youtube.com/watch?v=bOd3wO0uFr8&t=1453s for good UI design i suppose?

@Composable
fun NoteScreen(
    state: NoteState,
    mediaId: String,
    onEvent: (NoteEvent, String) -> Boolean?
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(state.notes.size) {
        val maxTabIndex = state.notes.size // because you always add one extra tab
        selectedTabIndex = selectedTabIndex.coerceIn(0..maxTabIndex)
    }


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
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (selectedTabIndex == state.notes.size) {
                item { // "add note" tab
                    OutlinedTextField(
                        value = state.title,
                        placeholder = {
                            Text(
                                "Untitled $selectedTabIndex",
                                fontSize = 24.sp
                            )
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
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .fillParentMaxHeight(0.8f)
                    )
                }
            } else {
                val note = state.notes.getOrNull(selectedTabIndex) // if a note exists, use this

                if(note != null) {
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
}

@Composable
fun TabRow(
    state: NoteState,
    selectedTabIndex: Int,
    onEvent: (NoteEvent, String) -> Boolean?,
    mediaId : String,
    onTabSelected: (Int) -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    PrimaryScrollableTabRow(
        selectedTabIndex = selectedTabIndex.coerceIn(0..state.notes.size),
        modifier = Modifier.padding(top = 24.dp)
    ) {
        state.notes.forEachIndexed { index, note ->
            Tab(
                selected = (selectedTabIndex == index),
                onClick = { onTabSelected(index) },
                text = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = note.title, fontSize = 16.sp)
                        IconButton(onClick = { onEvent(NoteEvent.DeleteNote(note), mediaId) }) {
                            Icon(Icons.Filled.Close, contentDescription = stringResource(
                                R.string.delete_note,
                                note.title
                            ))
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(R.string.add_note))
                    IconButton(onClick = {
                        val test = onEvent(NoteEvent.SaveNote, mediaId)

                        scope.launch {
                            if(test == true) {
                                SnackbarController.sendEvent(
                                    SnackbarEvent(
                                        message = context.getString(R.string.titles_of_notes_must_be_unique),
                                        action = SnackbarAction(
                                            name = "", // empty on purpose
                                            action = { } // empty on purpose
                                        ),
                                        duration = SnackbarDuration.Short
                                    )
                                )
                            }
                        }
                    }) {
                        Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.add_a_new_note))
                    }
                }
            }
        )
    }
}
