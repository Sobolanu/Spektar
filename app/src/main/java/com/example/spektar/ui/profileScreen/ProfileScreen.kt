package com.example.spektar.ui.profileScreen

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.spektar.data.model.User
import com.example.spektar.ui.common.components.BottomBar
import com.example.spektar.ui.userLoginScreens.ImagePicker
import com.example.spektar.ui.userLoginScreens.copyUriToFile

@Composable
fun ProfileScreen(
    onBottomBarItemClick: (Int) -> Unit,
    onEvent: (ProfileEvent) -> Unit,
    selectedIcon : Int,
    state: User // works
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val painter = if (selectedImageUri != null) {
        rememberAsyncImagePainter(selectedImageUri)
    } else {
        rememberAsyncImagePainter(state.avatar_url)
    }

    Scaffold(
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
            // uploads to storage but not to userid folder pls fix
            ImagePicker(  // make this look nice
                onImageSelected = { uri ->
                    selectedImageUri = uri
                    val image = context.copyUriToFile(uri)
                    onEvent(ProfileEvent.updateAvatar(state.id,image, state.username))
                },
                painter = painter
            ) // add some "Apply" box discord-style to this when you change the image

            Text( // make modifiable
                state.username,
                fontSize = 24.sp,
                modifier = Modifier.padding(top = 6.dp),
            )

            Button(
                // should move you to start of the app aswell
                onClick = { onEvent(ProfileEvent.signOut) }
            ) {
                Text(
                    "Sign out"
                )
            }

            Button(
                // should move you to start of the app aswell
                onClick = { /* onEvent(ProfileEvent.deleteAccount) */ }
            ) {
                Text(
                    "Delete account (onclick is empty for now)"
                )
            }

            Button(
                onClick = { /* onEvent(ProfileEvent.resetUserSuggestions) */ }
            ) {
                Text(
                    "Reset media recommendations"
                )
            }

            Button(

                onClick = { /* onEvent(ProfileEvent.resetPassword) */ }
            ) {
                Text(
                    "Reset password"
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
            Text("Profile")
        },

        modifier = Modifier.padding(bottom = 16.dp)
    )
}