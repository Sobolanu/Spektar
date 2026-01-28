package com.example.spektar.ui.profileScreen

import android.net.Uri
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.example.spektar.R
import com.example.spektar.data.model.User
import com.example.spektar.ui.common.components.BottomBar
import com.example.spektar.ui.userAuthScreens.signUpScreen.ImagePicker
import com.example.spektar.ui.userAuthScreens.signUpScreen.copyUriToFile
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    onBottomBarItemClick: (Int) -> Unit,
    onEvent: (ProfileEvent) -> Unit,
    selectedIcon : Int,
    state: User // works
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    // snackbar thing:
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // confirmation boxes:
    var accountDeletionConfirmationBox by remember { mutableStateOf(false) }
    var mediaRecommendationConfirmationBox by remember { mutableStateOf(false) }

    // sort out later
    val tempText = stringResource(R.string.snackbar_unsaved_changes)
    val tempTextTwo = stringResource(R.string.continue_dialog)

    val painter = if (selectedImageUri != null) {
        rememberAsyncImagePainter(selectedImageUri)
    } else {
        rememberAsyncImagePainter(state.avatar_url)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = { ProfileScreenTopBar() },
        bottomBar = {
            BottomBar(
                onBottomBarItemClick,
                selectedIcon,
            )
        },
        contentWindowInsets = WindowInsets(left = 8.dp)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            ImagePicker(  // make this look nice
                onImageSelected = { uri ->
                    selectedImageUri = uri
                    val image = context.copyUriToFile(uri)
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = tempText,
                            actionLabel = tempTextTwo,
                            duration = SnackbarDuration.Indefinite
                        )
                        when(result) {
                            SnackbarResult.ActionPerformed -> {
                                onEvent(ProfileEvent.updateAvatar(state.id,image, state.username))
                            }
                            SnackbarResult.Dismissed -> {
                                // will do nothing then
                            }
                        }
                    }
                },
                painter = painter
            ) // add some "Apply" box discord-style to this when you change the image

            Text( // make modifiable
                state.username,
                fontSize = 24.sp,
                modifier = Modifier.padding(top = 6.dp),
            )

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    onClick = { onEvent(ProfileEvent.signOut) },
                ) {
                    Text(
                        stringResource(R.string.sign_out),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "Save media to archive",
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    onClick = {
                        accountDeletionConfirmationBox = true
                    }
                ) {
                    Text(stringResource(R.string.delete_account),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "Save media to archive",
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    onClick = {
                        mediaRecommendationConfirmationBox = true
                    }
                ) {
                    Text(
                        stringResource(R.string.reset_media_recommendations),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "Save media to archive",
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ),

                    onClick = {
                        // onEvent(ProfileEvent.resetPassword)
                    }
                ) {
                    Text(
                        stringResource(R.string.reset_password),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "Save media to archive",
                    )
                }
            }

            if(accountDeletionConfirmationBox) {
                ProfileConfirmationDialog(
                    text = stringResource(R.string.account_deletion),
                    onContinueClick = { onEvent(ProfileEvent.deleteAccount) },
                    onDismissClick = { accountDeletionConfirmationBox = false }
                )
            }

            if(mediaRecommendationConfirmationBox) {
                ProfileConfirmationDialog(
                    text = stringResource(R.string.media_recommendation_dialog),
                    onContinueClick = {
                        onEvent(ProfileEvent.resetUserSuggestions)
                        mediaRecommendationConfirmationBox = false
                    },
                    onDismissClick = { mediaRecommendationConfirmationBox = false }
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ProfileScreenTopBar() {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),

        title = {
            Text(stringResource(R.string.account))
        },

        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileConfirmationDialog(
    text: String,
    onContinueClick: () -> Unit,
    onDismissClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissClick,
        text = { Text(text) },
        confirmButton = {
            Button(onClick = { onContinueClick() }) {
                Text(stringResource(R.string.continue_dialog))
            }
        },
        dismissButton = {
            Button(onClick = { onDismissClick() }) {
                Text(stringResource(R.string.dismiss))
            }
        }
    )
}