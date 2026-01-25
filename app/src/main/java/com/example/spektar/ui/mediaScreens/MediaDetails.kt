package com.example.spektar.ui.mediaScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.spektar.domain.model.SpecificMedia
import com.example.spektar.ui.common.components.navigationBarIcons.topProfileIcon
import com.example.spektar.ui.common.components.navigationBarIcons.topBackArrowIcon

@Composable
fun MediaDetailsScreen(
    goToProfile: () -> Unit,
    onBackClick: () -> Unit,
    onNoteButtonClick: (String) -> Unit,
    state: SpecificMedia,
) {
    Scaffold(
        topBar = { DetailsPageTopBar(goToProfile, onBackClick) },
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

                    model = state.imageUrl,
                    contentDescription = "Image of the media ${state.name}"
                )
            }

            // review thingamajig

            item {
                Text(
                    modifier = Modifier.padding(vertical = 24.dp),
                    text = state.name,
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
                    state.description,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )
            }

            item {
                Button(
                    onClick = {
                        onNoteButtonClick(state.id_uuid)
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Notes")
                }
            }

            item {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "by ${state.credits}",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "released on ${state.release_date}",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsPageTopBar(
    goToProfile: () -> Unit,
    onBackClick: () -> Unit,
    modifier : Modifier = Modifier
) {
    val backButtonPressed by remember {mutableStateOf(false)}
    val profileIconPressed by remember {mutableStateOf(false)}

    TopAppBar(
        modifier = modifier,

        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF7E0101),
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),

        title = { Text("test") },

        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = if(backButtonPressed) {
                        topBackArrowIcon.selectedIcon
                    } else {
                        topBackArrowIcon.unselectedIcon
                    },

                    contentDescription = stringResource(topBackArrowIcon.title)
                )
            }
        },

        actions = {
            IconButton(
                onClick = { goToProfile() } // figure out navigation to profile page
            ) {
                Icon(
                    imageVector = if(profileIconPressed) {
                        topProfileIcon.selectedIcon
                    } else {
                        topProfileIcon.unselectedIcon
                    },

                    contentDescription = stringResource(topProfileIcon.title)
                )
            }
        }
    )
}