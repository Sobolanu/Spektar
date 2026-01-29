package com.example.spektar.ui.userAuthScreens.loginScreen

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.spektar.R
import com.example.spektar.ui.common.SnackbarAction
import com.example.spektar.ui.common.SnackbarController
import com.example.spektar.ui.common.SnackbarEvent
import com.example.spektar.ui.userAuthScreens.AuthEvent
import com.example.spektar.ui.userAuthScreens.states.SignInState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun UserLoginScreen(
    onSignInClick: () -> Unit,
    onTextClick: () -> Unit,
    state : SignInState,
    showEmailPopUp : Boolean,
    onEvent: (AuthEvent) -> Unit
) {
    if(state.signInFinished) {
        onSignInClick()
    }

    val scope = rememberCoroutineScope()
    var visible by remember { mutableStateOf(false) }
    var showEmail by remember { mutableStateOf(showEmailPopUp) }

    if(state.snackBarText != null) {
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

    LaunchedEffect(Unit) {
        delay(100) // hardcoded 100ms delay
        visible = true
    }

    Scaffold(
        contentWindowInsets = WindowInsets(
            left = 16.dp,
            top = 72.dp,
            right = 16.dp,
            bottom = 16.dp
        )
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),

            horizontalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = expandHorizontally() + fadeIn(),
                exit = shrinkHorizontally() + fadeOut()
            ) {
                Text(
                    text = stringResource(R.string.app_greeting),
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Justify
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painterResource(R.drawable.app_logo_transparent_old),
                contentDescription = stringResource(R.string.logo_description),
                modifier = Modifier.size(250.dp)
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
                placeholder = { Text(stringResource(R.string.email)) },
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
                placeholder = { Text(stringResource(R.string.password)) },
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Button(
                enabled = !state.signInFinished,
                onClick = {
                    if(state.email != "" && state.password != "") {
                        onEvent(AuthEvent.SignIn(state))
                    }
                }
            ) {
                Text(
                    stringResource(R.string.sign_in)
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
                    text = stringResource(R.string.registration_prompt)
                )

                Text(
                    modifier = Modifier.clickable(
                        onClick = onTextClick
                    ),

                    text = stringResource(R.string.register)
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
                    stringResource(R.string.email_confirmation),
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