package com.example.spektar.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.spektar.R

@Composable
fun CreateUserScreen(
    onTextClick: () -> Unit
) {
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
            Text(
                text = "Welcome to Spektar.",
                style = MaterialTheme.typography.headlineLarge,
            )
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
                state = rememberTextFieldState(),
                leadingIcon = { Icon(
                    Icons.Filled.AccountCircle,
                    contentDescription = "Username",
                ) },
                placeholder = { Text("Username/Email") },
                modifier = Modifier.padding(bottom = 12.dp)
            )

            TextField(
                state = rememberTextFieldState(),
                leadingIcon = { Icon(
                    Icons.Filled.Key,
                    contentDescription = "Password",
                ) },
                placeholder = { Text("Password") },
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = buildClickableText(
                    text = "Don't have an account? Register now",
                    clickableText = "Register now",
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    tag = "bla",
                    onClick = { onTextClick } // make this go into register screen cause linkInteractionListener highk a bitch
                )
            )

            Button(
                onClick = onTextClick // navigate to some screen, for now as placeholder let it navigate to media category
            ) {
                Text(
                    "placeholder button"
                )
            }

        }
    }
}

fun buildClickableText(
    text: String, // The full text including the specific text
    clickableText: String, // The specific text you want to be clickable
    style: SpanStyle,
    tag: String,
    onClick: ((link: LinkAnnotation) -> Unit)? = null
): AnnotatedString {
    return buildAnnotatedString {
        val startIndex = text.indexOf(clickableText)
        val endIndex = startIndex + clickableText.length

        withStyle(style = style) { append(text) }
        addLink(
            LinkAnnotation.Clickable(
                tag = tag,
                styles = TextLinkStyles(
                    style = style.copy(/* update as needed */)
                ),
                linkInteractionListener = onClick
            ),
            startIndex,
            endIndex
        )
    }
}
