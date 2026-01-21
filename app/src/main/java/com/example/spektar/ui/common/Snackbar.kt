package com.example.spektar.ui.common

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.example.spektar.ui.profileScreen.ProfileEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
/*
@Composable
fun SnackBar(
    onConfirmEvent: () -> Unit,
    onDismissEvent: () -> Unit,
    scope: CoroutineScope = rememberCoroutineScope(),

    text: String,
    event: () -> Unit,
) {
    scope.launch {
        val result = snackbarHostState.showSnackbar(
            message = text,
            actionLabel = "Save",
            duration = SnackbarDuration.Indefinite
        )
        when(result) {
            SnackbarResult.ActionPerformed -> {
                onEvent(ProfileEvent.updateAvatar(state.id,image, state.username))
            }
            SnackbarResult.Dismissed -> {
                /* Handle snackbar dismissed */
            }
        }
    }
}
*/