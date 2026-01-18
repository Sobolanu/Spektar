package com.example.spektar.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spektar.data.model.Note
import com.example.spektar.data.model.NoteState
import com.example.spektar.domain.repository.NoteEvent

// refer to https://www.youtube.com/watch?v=bOd3wO0uFr8&t=1453s for good UI design i suppose?
// TODO: save the data upon "adding" a note

@Composable
fun NoteScreen(
    state : NoteState,
    onEvent: (NoteEvent) -> Unit
) {
    Scaffold(
        topBar = { TabRow(state.notes, onEvent) }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            // defaults, could be modified though i suppose by the user?
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                TextField(
                    value = state.title,
                    onValueChange = {
                        onEvent(NoteEvent.SetTitle(it))
                    },
                )

                Spacer(modifier = Modifier.padding(vertical = 16.dp))
            }
            item {
                TextField(
                    value = state.text,
                    onValueChange = {
                        onEvent(NoteEvent.SetText(it))
                    },
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .fillParentMaxHeight(0.8f)
                )
            }
        }
    }
}

@Composable
fun TabRow(
    notes: List<Note>,
    onEvent: (NoteEvent) -> Unit
) {
    var selectedTabIndex by remember { // this may need to go into mainactivity but we shall see
        mutableIntStateOf(0)
    }

    var addNewNote by remember {
        mutableStateOf(false)
    }

    PrimaryScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = Modifier.padding(top = 24.dp)
    ) {
        notes.forEachIndexed{ index, note ->
            Tab(
                selected = ( selectedTabIndex == index ),
                onClick = { selectedTabIndex = index },

                text = { // i put every composable inside of "text", because the tab.icon forces the icon to be super small
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = note.title,
                            fontSize = 16.sp
                        )
                        IconButton(
                            onClick = { onEvent(NoteEvent.DeleteNote(note)) },
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = null,
                            )
                        }
                    }
                }
            )
        }

        Tab(
            selected = ( addNewNote ),
            onClick = {
                addNewNote = !addNewNote
                onEvent(NoteEvent.SaveNote)
            },

            text = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Add note",
                        // modifier = Modifier.weight(3f) // 3/4 of width
                    )
                    IconButton(
                        onClick = { onEvent(NoteEvent.SaveNote) },
                        // modifier = Modifier.weight(1f) // 1/4 of width
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = null,
                            // modifier = Modifier.fillMaxSize() // fills its weight space
                        )
                    }
                }
            }
        )
    }
}