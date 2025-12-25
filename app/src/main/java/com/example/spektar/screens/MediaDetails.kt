package com.example.spektar.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.spektar.models.notes.room.database.NoteEvent
import com.example.spektar.models.topBackArrowIcon
import com.example.spektar.models.topProfileIcon
import com.example.spektar.viewmodels.MediaViewModel

@Composable
fun MediaDetailsScreen(
    onBackClick: () -> Unit,
    // onEvent: (NoteEvent) -> Unit, implement navigation to notes for specific media

    mediaPosition: Pair<Int, Int>,
    viewModel : MediaViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { DetailsPageTopBar(onBackClick) },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF110205)), // implement custom color scheme no material3 cause that shit is hot ASS.

            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                AsyncImage(
                    modifier = Modifier
                        .size(200.dp)
                        .padding(top = 16.dp)
                        .border(
                            width = 8.dp,
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ),

                    model = uiState.medias[mediaPosition.first][mediaPosition.second].imageUrl,
                    contentDescription = null
                )
            }

            // review thingamajig

            item {
                Text(
                    modifier = Modifier
                        .padding(vertical = 24.dp),
                    text = uiState.medias[mediaPosition.first][mediaPosition.second].name,
                    autoSize = TextAutoSize.StepBased(
                        16.sp,
                        38.sp,
                        2.sp
                    ),

                    lineHeight = 46.sp,
                    textAlign = TextAlign.Center
                )
            }

            item {
                Text(
                    uiState.medias[mediaPosition.first][mediaPosition.second].description,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )
            }

            item {
                Button(
                    onClick = {
                        // here pass onEvent(NoteEvent.something), this would actually be navigation to notedetails screen
                    }
                ) { }
            }
            // add notes item (item that leads to a notes page for THIS SPECIFIC PIECE of media)
            // notes item should be a local database!!!
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun DetailsPageTopBar(
    onBackClick: () -> Unit,
    modifier : Modifier = Modifier
) {
    val backButtonPressed = false
    val profileIconPressed = false

    TopAppBar(
        modifier = modifier,

        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF7E0101),
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),

        title = {Text("test")},

        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = if(backButtonPressed) {
                        topBackArrowIcon.selectedIcon
                    } else {
                        topBackArrowIcon.unselectedIcon
                    },

                    contentDescription = topBackArrowIcon.title
                )
            }
        },

        actions = {
            IconButton(
                onClick = {} // figure out navigation to profile page
            ) {
                Icon(
                    imageVector = if(profileIconPressed) {
                        topProfileIcon.selectedIcon
                    } else {
                        topProfileIcon.unselectedIcon
                    },

                    contentDescription = topProfileIcon.title
                )
            }
        }
    )
}