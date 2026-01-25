package com.example.spektar.ui.common.components

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.spektar.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DebouncedTextField(
    text: String,
    onTextChange: (String) -> Unit,
    onEvent: (String) -> Unit
) {
    var internalText by remember { mutableStateOf(text) }
    val coroutineScope = rememberCoroutineScope()
    var debounceJob by remember { mutableStateOf<Job?>(null) }

    TextField(
        value = internalText,
        maxLines = 1,
        onValueChange = { newText ->
            internalText = newText
            onTextChange(newText)

            // Cancel any previous debounce job
            debounceJob?.cancel()
            if(internalText != "" && newText != "") {
                debounceJob = coroutineScope.launch {
                    delay(500) // half a second debounce
                    onEvent(newText)
                }
            }
        },
        placeholder = {
            Text(stringResource(R.string.search))
        }
    )
}
