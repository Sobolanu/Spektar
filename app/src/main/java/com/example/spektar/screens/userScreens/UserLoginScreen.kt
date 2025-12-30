package com.example.spektar.screens.userScreens

import androidx.activity.viewModels
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.spektar.R
import kotlinx.coroutines.delay
import androidx.lifecycle.viewmodel.compose.viewModel

// make registration screen
@Composable
fun CreateUserScreen(
    onTextClick: () -> Unit,
    // viewModel: SignInViewModel = viewModel()
) {
    val usernameState = rememberTextFieldState()
    val emailState = rememberTextFieldState()
    val passwordState = rememberTextFieldState()

    var visible by remember {mutableStateOf(false)}

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
                state = usernameState,
                leadingIcon = { Icon(
                    Icons.Filled.AccountCircle,
                    contentDescription = "Username",
                ) },
                placeholder = { Text("Username") },
                modifier = Modifier.padding(bottom = 12.dp)
            )

            TextField(
                state = emailState,
                leadingIcon = { Icon(
                    Icons.Filled.Key,
                    contentDescription = "Email",
                ) },
                placeholder = { Text("Email") },
                modifier = Modifier.padding(bottom = 12.dp)
            )

            TextField(
                state = passwordState,
                leadingIcon = { Icon(
                    Icons.Filled.Key,
                    contentDescription = "Password",
                ) },
                placeholder = { Text("Password") },
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Button(
                onClick = {

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
                    modifier = Modifier.
                        padding(end = 8.dp),

                    text = "Don't have an account?"
                )

                Text(
                    // figure out some nice color for this text to signify it's importance.
                    modifier = Modifier.clickable(
                        onClick = onTextClick
                    ),

                    text = "Register now."
                )
            }
        }
    }
}