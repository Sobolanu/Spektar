package com.example.spektar.ui.userLoginScreens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.spektar.R
import com.example.spektar.ui.viewModels.states.SignInRequest
import kotlinx.coroutines.delay

@Composable
fun UserLoginScreen(
    onSignInClick: () -> Unit,
    onTextClick: () -> Unit,
    state : SignInRequest,
    showEmailPopUp : Boolean,
    onEvent: (AuthEvent) -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    var showEmail by remember { mutableStateOf(showEmailPopUp) }

    LaunchedEffect(Unit) {
        delay(100) // hardcoded 100ms delay
        visible = true
    }

    Scaffold(
        contentWindowInsets = WindowInsets(
            left = 16.dp,
            top = 48.dp,
            right = 16.dp,
            bottom = 16.dp
        )
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = expandHorizontally() + fadeIn(),
                exit = shrinkHorizontally() + fadeOut()
            ) {
                Text(
                    text = "Welcome to Spektar.",
                    style = MaterialTheme.typography.headlineLarge,
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painterResource(R.drawable.ic_launcher_background), // placeholder for now, add logo of the app
                contentDescription = "Spektar logo."
            )

            Spacer(
                modifier = Modifier.padding(vertical = 16.dp)
            )

            TextField(
                value = state.email,
                onValueChange = { email ->
                    onEvent(AuthEvent.SetEmail(email))
                },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Email,
                        contentDescription = "Email",
                    )
                },
                placeholder = { Text("Email") },
                modifier = Modifier.padding(bottom = 12.dp)
            )

            TextField(
                value = state.password,
                onValueChange = { password ->
                    onEvent(AuthEvent.SetPassword(password))
                },

                leadingIcon = {
                    Icon(
                        Icons.Filled.Key,
                        contentDescription = "Password",
                    )
                },
                placeholder = { Text("Password") },
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Button(
                onClick = {
                    onEvent(AuthEvent.SignIn(state))
                    onSignInClick()
                }
            ) {
                Text(
                    "Sign in"
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),

                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(end = 8.dp),
                    text = "Don't have an account?"
                )

                Text(
                    // figure out some nice color for this text to signify it's importance.
                    modifier = Modifier.clickable(
                        onClick = onTextClick
                    ),

                    text = "Register now."
                )

                if (showEmail) {
                    ConfirmationDialog(onDismissRequest = { showEmail = false })
                }
            }
        }
    }
}

@Composable
fun ConfirmationDialog(
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "A confirmation mail has been sent to your email account. Please confirm before proceeding.",
                    fontSize = 18.sp
                )
                Spacer(Modifier.height(12.dp))

                Button(onClick = { onDismissRequest() }) {
                    Text("Close")
                }
            }
        }
    }
}
