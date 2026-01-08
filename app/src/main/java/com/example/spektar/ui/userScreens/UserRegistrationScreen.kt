package com.example.spektar.ui.userScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.spektar.ui.viewModels.SignUpViewModel

@Composable
fun UserRegistrationScreen(
    viewModel : SignUpViewModel = viewModel(),
    onSignUp : () -> Unit
) {
    val signUpState by viewModel.signUpState.collectAsState()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = signUpState.email,
                onValueChange = { newValue ->
                    viewModel.updateEmail(newValue)
                },
                leadingIcon = { Icon(
                    Icons.Filled.Email,
                    contentDescription = "Email",
                ) },
                placeholder = { Text("Email") },
                modifier = Modifier.padding(bottom = 12.dp)
            )

            TextField(
                value = signUpState.password,
                onValueChange = { newValue ->
                    viewModel.updatePassword(newValue)
                },
                leadingIcon = { Icon(
                    Icons.Filled.Key,
                    contentDescription = "Password",
                ) },
                placeholder = { Text("Password") },
                modifier = Modifier.padding(bottom = 12.dp)
            )

            /*
                TextField( // confirm password dialogue, implement later
                value = signUpState.password,
                onValueChange = { newValue ->
                    viewModel.updatePassword(newValue)
                },
                leadingIcon = { Icon(
                    Icons.Filled.Key,
                    contentDescription = "Password",
                ) },
                placeholder = { Text("Password") },
                modifier = Modifier.padding(bottom = 12.dp)
                )
            */

            Row(
                modifier = Modifier.padding(paddingValues),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        viewModel.onSignUpClick()
                        onSignUp() // move to home screen but i'll add a placeholder for now
                    }
                ) {
                    Text(
                        "Continue"
                    )

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Continue"
                    )
                }
            }
        }
    }
}