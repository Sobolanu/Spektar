package com.example.spektar.ui.userAuthScreens.signUpScreen

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.spektar.R
import com.example.spektar.ui.common.SnackbarAction
import com.example.spektar.ui.common.SnackbarController
import com.example.spektar.ui.common.SnackbarEvent
import com.example.spektar.ui.userAuthScreens.AuthEvent
import com.example.spektar.ui.userAuthScreens.states.SignUpState
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun UserRegistrationScreen(
    state: SignUpState,
    onSignUp : () -> Unit,
    onEvent: (AuthEvent) -> Unit
) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if(state.isLoading) {
                CircularProgressIndicator()
            } else {
                if(state.signUpFinished) {
                    onSignUp()
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val context = LocalContext.current
                    val scope = rememberCoroutineScope()

                    var selectedImageUri by remember {
                        mutableStateOf<Uri?>(Uri.parse("android.resource://${context.packageName}/${R.drawable.blank_profile_picture}"))
                    }

                    if (state.snackBarText != null) {
                        scope.launch {
                            SnackbarController.sendEvent(
                                SnackbarEvent(
                                    message = state.snackBarText,
                                    action = SnackbarAction("", { }), // empty on purpose
                                    duration = SnackbarDuration.Short
                                )
                            )
                        }
                    }

                    val painter = if (selectedImageUri != null) {
                        rememberAsyncImagePainter(selectedImageUri)
                    } else {
                        painterResource(R.drawable.blank_profile_picture) // add default gray profile
                    }

                    LaunchedEffect(selectedImageUri) {
                        selectedImageUri?.let { uri ->
                            val image = context.copyUriToFile(uri)
                            onEvent(AuthEvent.SetAvatar(image))
                        }
                    }

                    ImagePicker(
                        onImageSelected = { uri ->
                            selectedImageUri = uri
                            val image = context.copyUriToFile(uri)
                            onEvent(AuthEvent.SetAvatar(image))
                        },

                        painter = painter
                    )

                    TextField(
                        value = state.username,
                        onValueChange = { newUsername ->
                            onEvent(AuthEvent.SetUsername(newUsername))
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Filled.AccountCircle,
                                contentDescription = "Username",
                            )
                        },
                        placeholder = { Text(stringResource(R.string.username)) },
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    TextField(
                        value = state.email,
                        onValueChange = { newEmail ->
                            onEvent(AuthEvent.SetEmail(newEmail))
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Filled.Email,
                                contentDescription = "Email",
                            )
                        },
                        placeholder = { Text(stringResource(R.string.email)) },
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    TextField(
                        value = state.password,
                        onValueChange = { newPassword ->
                            onEvent(AuthEvent.SetPassword(newPassword))
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Filled.Key,
                                contentDescription = "Password",
                            )
                        },
                        placeholder = { Text(stringResource(R.string.password)) },
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Row(
                        modifier = Modifier.padding(paddingValues),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            enabled = !state.signUpFinished,

                            onClick = {
                                if (state.email != "" && state.password != "") {
                                    onEvent(AuthEvent.SignUp(state))
                                }
                            }
                        ) {
                            Text(
                                stringResource(R.string.create_account)
                            )

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Make an account"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ImagePicker(
    onImageSelected: (Uri) -> Unit,
    painter : Painter
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            onImageSelected(uri)
        }
    }

    Card(
        onClick = { launcher.launch("image/*") },
        modifier = Modifier.padding(bottom = 12.dp)
    ) {
        Image(
            painter = painter,
            contentDescription = "Your profile picture",
            modifier = Modifier
                .size(175.dp),

            contentScale = ContentScale.Crop
        )
    }
}

fun Context.copyUriToFile(uri: Uri): File {
    val inputStream = contentResolver.openInputStream(uri)
        ?: throw IllegalArgumentException("Cannot open input stream from URI")

    val tempFile = File(cacheDir, "avatar_${System.currentTimeMillis()}.jpg")
    tempFile.outputStream().use { output ->
        inputStream.copyTo(output)
    }

    return tempFile
}
