package com.example.spektar.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.spektar.R
import com.example.spektar.data.model.roomModels.Media
import com.example.spektar.ui.archiveScreen.ArchiveEvent
import com.example.spektar.ui.common.components.BottomBar
import com.example.spektar.ui.common.components.navigationBarIcons.topProfileIcon
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    goToQuestionnaireScreen: () -> Unit,
    goToProfile: () -> Unit,
    onEvent: (ArchiveEvent) -> Unit,
    goToArchiveScreen: () -> Unit,
    selectedIcon: Int,
    onBottomBarItemClick: (Int) -> Unit,
    state: List<Media>
) {
    val scrollBehavior = enterAlwaysScrollBehavior()

    Scaffold(
        topBar =  {
            HomePageTopBar(
                goToProfile = goToProfile,
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            BottomBar(
                selectedIcon = selectedIcon,
                onBottomBarItemClick = onBottomBarItemClick
            )
        },
        contentWindowInsets = WindowInsets(left = 8.dp, right = 8.dp)
    ) { paddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                Image(
                    painterResource(R.drawable.app_logo_transparent),
                    contentDescription = stringResource(R.string.logo_description)
                )

                Spacer(modifier = Modifier.padding(bottom = 32.dp))
            }

            if(state.isEmpty()) {
                item {
                    Text(
                        "Save a few medias to populate the home screen!",
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                items(state) {
                    HomePageMedia(
                        it,
                        onEvent = { progress, id ->
                            onEvent(ArchiveEvent.updateProgress(progress, id))
                        }
                    )
                }
            }


            item{
                Button(
                    onClick = { goToArchiveScreen() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Text(
                        "Go to archive",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            item {
                Button(
                    onClick = { goToQuestionnaireScreen() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Text(
                        stringResource(R.string.fill_out_questionnaire),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun HomePageMedia(
    media: Media,
    onEvent: (Int, String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        UpdateProgressDialog(
            onDismiss = { showDialog = false },
            onSubmit = {
                onEvent(it, media.id_uuid)
                showDialog = false
            }
        )
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = media.name,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(110.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    AsyncImage(
                        model = media.imageUrl,
                        contentDescription = media.name,
                        modifier = Modifier
                            .size(110.dp)
                            .clip(MaterialTheme.shapes.medium),
                        contentScale = ContentScale.Crop
                    )
                }

                Text(
                    "Update progress",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Box(
                    modifier = Modifier.size(110.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val fraction = remember(media.currentProgress, media.totalSize) {
                        ((media.currentProgress.toFloat() / media.totalSize.toFloat()).coerceIn(0f, 1f))
                    }

                    val animatedProgress by animateFloatAsState(targetValue = fraction)

                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(100.dp)) {
                        CircularProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier.fillMaxSize()
                        )
                        Text(
                            text = "${(animatedProgress * 100).roundToInt()}%",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Text(
                    "Completed: ${media.currentProgress} of ${media.totalSize}",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
    }
}

@Composable
fun UpdateProgressDialog(
    onDismiss: () -> Unit,
    onSubmit: (newProgress : Int) -> Unit
) {
    var amount by remember { mutableStateOf("") }

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
                    text = "How much have you completed?",
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Your new progress") },
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
                    Button(onClick = { onSubmit(amount.toInt()) }) {
                        Text("Submit")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePageTopBar(
    goToProfile: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val iconButtonPressed by remember {mutableStateOf(false)}

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),

        title = {
            Text(
                text = "Home",
                style = MaterialTheme.typography.titleLarge
            )
        },

        scrollBehavior = scrollBehavior,

        actions = {
            IconButton(
                onClick = { goToProfile() }
            ) {
                Icon(
                    imageVector = if (iconButtonPressed) {
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