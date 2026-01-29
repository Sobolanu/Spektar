package com.example.spektar.ui.mediaScreens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
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
import com.example.spektar.data.remote.mediaService.ReviewData
import com.example.spektar.ui.common.components.navigationBarIcons.topBackArrowIcon
import com.example.spektar.ui.common.components.navigationBarIcons.topProfileIcon

@Composable
fun MediaDetailsScreen(
    goToProfile: () -> Unit,
    onBackClick: () -> Unit,
    leaveReview: (String, Int, String) -> Unit,
    onNoteButtonClick: (String) -> Unit,
    saveMedia: (Media) -> Unit,
    state: FullMediaData,
    isArchived: Boolean,
    reviews : List<ReviewData>
) {
    var openDialog by remember { mutableStateOf(false) }
    var openGoalDialog by remember { mutableStateOf(false) }
    var checkReviewsDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { DetailsPageTopBar(goToProfile, onBackClick) },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), // implement custom color scheme no material3 cause that shit is hot ASS.

            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(openDialog) {
                item {
                    ReviewDialog(
                        onDismiss = { openDialog = false },
                        onSubmit = { rating, message ->
                            leaveReview(state.id_uuid, rating, message)
                            openDialog = false
                        }
                    )
                }
            }

            if(checkReviewsDialog) {
                item {
                    AllReviewsDialog(
                        onDismiss = { checkReviewsDialog = false },
                        reviews = reviews
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
                                    totalSize = finalGoal,
                                    currentProgress = 0
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

                        onClick = { openGoalDialog = true }
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

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Button(
                        onClick = { checkReviewsDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onBackground
                        )
                    ) {
                        Text(
                            "Check reviews",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = "Check reviews",
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
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer
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
                        Text(stringResource(R.string.dismiss))
                    }

                    Button(onClick = { onSubmit(setDailyGoal, dailyGoal.toInt(), totalGoal.toInt()) }) {
                        Text(stringResource(R.string.continue_dialog))
                    }
                }
            }
        }
    }
}

@Composable
fun AllReviewsDialog(
    onDismiss: () -> Unit,
    reviews: List<ReviewData>
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            // Limit height so the list can scroll
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .sizeIn(maxHeight = 400.dp) // adjust as needed
                    .padding(16.dp)
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(reviews) { data ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            // Header: user id + stars
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = data.username,
                                    modifier = Modifier.weight(1f)
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    (1..5).forEach { star ->
                                        Icon(
                                            imageVector = if (star <= data.rating) Icons.Default.Star else Icons.Default.StarBorder,
                                            contentDescription = "Rate $star stars",
                                            tint = if (star <= data.rating) Color(0xFFFFD700) else Color.Gray,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))

                            // Review text on its own line
                            Text(
                                text = data.review_text,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        HorizontalDivider()
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
