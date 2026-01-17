package com.example.spektar.ui.common.modifiers

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Modifier.debouncedClickable(
    delayMillis: Long = 500,
    onClick: () -> Unit
): Modifier {
    var enabled by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    return clickable(enabled) {
        if (enabled) {
            enabled = false
            onClick()
            scope.launch {
                delay(delayMillis)
                enabled = true
            }
        }
    }
}
