package com.example.spektar.ui.common.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// might be useless
@Composable
fun DebouncedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    delayMillis: Long = 500,
    content: @Composable RowScope.() -> Unit
) {
    var disabled by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Button(
        onClick = {
            if (!disabled) {
                disabled = true
                onClick()
                scope.launch {
                    delay(delayMillis)
                    disabled = false
                }
            }
        },
        enabled = enabled && !disabled,
        modifier = modifier,
        content = content
    )
}
