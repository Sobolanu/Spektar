package com.example.spektar.ui.mediaScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.spektar.R
import com.example.spektar.data.model.roomModels.Media
import com.example.spektar.data.remote.mediaService.FullMediaData
import com.example.spektar.domain.model.SpecificMedia
import com.example.spektar.ui.archiveScreen.ArchiveEvent
import com.example.spektar.ui.common.components.navigationBarIcons.topProfileIcon
import com.example.spektar.ui.common.components.navigationBarIcons.topBackArrowIcon

@Composable
fun MediaDetailsScreen(
    goToProfile: () -> Unit,
    onBackClick: () -> Unit,
    leaveReview: (String, Int, String) -> Unit,
    onNoteButtonClick: (String) -> Unit,
    //saveMedia: (SpecificMedia) -> Unit,
    saveMedia: (Media) -> Unit,
    state: FullMediaData,
    isArchived: Boolean
) {
    var openDialog by remember { mutableStateOf(false) }
    var openGoalDialog by remember { mutableStateOf(false) }

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
            if(openDialog) {
                item {
                    ReviewDialog(
                        onDismiss = { openDialog = false },
                        onSubmit = { rating, message ->
                            leaveReview(state.id_uuid, rating, message)
                        }
                    )
                }
            }

            if(openGoalDialog) {
                item{
                    DailyGoalDialog(
                        onDismiss = { openGoalDialog = false },
                        onSubmit = { setDailyGoal, dailyGoal, finalGoal ->
                            saveMedia(
                                Media(
                                    id_uuid = state.id_uuid,
                                    name = state.name,
                                    imageUrl = state.imageUrl,
                                    description = state.description,
                                    credits = state.credits,
                                    release_date = state.release_date,
                                    daily_goal_set = setDailyGoal,
                                    dailyGoal = dailyGoal,
                                    totalSize = finalGoal
                                )
                            )

                            openGoalDialog = false
                        }
                    )
                }
            }
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

                Text(
                    text = "(${state.rating_count})",
                    color = Color.Gray,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            item {
                Row(horizontalArrangement = Arrangement.Center) {
                    (1..5).forEach { star ->
                        Icon(
                            imageVector = if (star <= state.average_rating) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "${state.name} is rated at ${state.average_rating}",
                            tint = if (star <= state.average_rating) Color(0xFFFFD700) else Color.Gray,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }
            }

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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
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

                Spacer(
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            item {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Button(
                        onClick = {
                            onNoteButtonClick(state.id_uuid)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onBackground
                        )
                    ) {
                        Text(
                            "Notes",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = "Check notes",
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onBackground
                        ),

                        onClick = {
                            openGoalDialog = true
                        }
                    ) {
                        Text(
                            "Save media",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = "Save media to archive",
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Button(
                        onClick = { openDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onBackground
                        )
                    ) {
                        Text(
                            "Leave a review",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = "Leave a review.",
                        )
                    }
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

@Composable
fun DailyGoalDialog(
    onDismiss: () -> Unit,
    onSubmit: (Boolean, Int, Int) -> Unit
) {
    var setDailyGoal by remember { mutableStateOf(false) }
    var dailyGoal by remember { mutableStateOf("") }
    var totalGoal by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Do you want to set a daily goal?",
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Checkbox(
                        checked = setDailyGoal,
                        onCheckedChange = { it ->
                            setDailyGoal = it
                        }
                    )
                }

                if(setDailyGoal) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Set a daily goal (in pages or episodes):"
                        )

                        OutlinedTextField(
                            value = dailyGoal,
                            label = { Text("Daily goal") },
                            onValueChange = {
                                dailyGoal = it
                            }
                        )

                        Text(
                            text = "How long is the media (in pages or episodes):"
                        )

                        OutlinedTextField(
                            value = totalGoal,
                            label = { Text("Final goal") },
                            onValueChange = {
                                totalGoal = it
                            }
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }

                    Button(onClick = { onSubmit(setDailyGoal, dailyGoal.toInt(), totalGoal.toInt()) }) {
                        Text("Submit")
                    }
                }
            }
        }
    }
}

@Composable
fun ReviewDialog(
    onDismiss: () -> Unit,
    onSubmit: (rating: Int, message: String) -> Unit
) {
    var rating by remember { mutableStateOf(0) }
    var message by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Leave a Review",
                    style = MaterialTheme.typography.titleMedium
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    (1..5).forEach { star ->
                        IconButton(onClick = { rating = star }) {
                            Icon(
                                imageVector = if (star <= rating) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = "Rate $star stars",
                                tint = if (star <= rating) Color(0xFFFFD700) else Color.Gray
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Your message") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 4
                )

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Button(onClick = { onSubmit(rating, message) }) {
                        Text("Submit")
                    }
                }
            }
        }
    }
}
