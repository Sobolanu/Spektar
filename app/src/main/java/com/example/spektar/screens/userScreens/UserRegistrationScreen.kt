package com.example.spektar.screens.userScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UserRegistrationScreen() {
    Scaffold(

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                state = rememberTextFieldState(),
                leadingIcon = { Icon(
                    Icons.Filled.AccountCircle,
                    contentDescription = "Username",
                ) },
                placeholder = { Text("Username") },
                modifier = Modifier.padding(bottom = 12.dp)
            )

            TextField(
                state = rememberTextFieldState(),
                leadingIcon = { Icon(
                    Icons.Filled.AccountCircle,
                    contentDescription = "Email",
                ) },
                placeholder = { Text("Email") },
                modifier = Modifier.padding(bottom = 12.dp)
            )

            TextField(
                state = rememberTextFieldState(),
                leadingIcon = { Icon(
                    Icons.Filled.AccountCircle,
                    contentDescription = "Username",
                ) },
                placeholder = { Text("Password") },
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.padding(paddingValues),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { true }
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