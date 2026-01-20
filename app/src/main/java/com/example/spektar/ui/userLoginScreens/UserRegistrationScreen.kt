package com.example.spektar.ui.userLoginScreens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.spektar.R
import com.example.spektar.ui.viewModels.states.SignUpRequest
import java.io.File

// val file = uri.toFile()
@Composable
fun UserRegistrationScreen(
    state: SignUpRequest,
    onSignUp : () -> Unit,
    onEvent: (AuthEvent) -> Unit
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
            val context = LocalContext.current

            val painter = if (selectedImageUri != null) {
                rememberAsyncImagePainter(selectedImageUri)
            } else {
                painterResource(R.drawable.ic_launcher_foreground) // add default gray profile
            }

            ImagePicker(  // make this look nice
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
                leadingIcon = { Icon(
                    Icons.Filled.AccountCircle,
                    contentDescription = "Username",
                ) },
                placeholder = { Text("Username") },
                modifier = Modifier.padding(bottom = 12.dp)
            )

            TextField(
                value = state.email,
                onValueChange = { newEmail ->
                    onEvent(AuthEvent.SetEmail(newEmail))
                },
                leadingIcon = { Icon(
                    Icons.Filled.Email,
                    contentDescription = "Email",
                ) },
                placeholder = { Text("Email") },
                modifier = Modifier.padding(bottom = 12.dp)
            )

            TextField(
                value = state.password,
                onValueChange = { newPassword ->
                    onEvent(AuthEvent.SetPassword(newPassword))
                },
                leadingIcon = { Icon(
                    Icons.Filled.Key,
                    contentDescription = "Password",
                ) },
                placeholder = { Text("Password") },
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.padding(paddingValues),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        onEvent(AuthEvent.SignUp(state))
                        onSignUp() // move to login screen and show an indicator for "email has been sent to your mail account, confirm to use the app"
                    }
                ) {
                    Text(
                        "Continue"
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

// launcher.launch("image/*") inside of your onClick
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
                .size(175.dp), // make circular maybe?

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
