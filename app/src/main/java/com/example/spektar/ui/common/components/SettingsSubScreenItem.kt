package com.example.spektar.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.spektar.R
import com.example.spektar.ui.common.SnackbarAction
import com.example.spektar.ui.common.SnackbarController
import com.example.spektar.ui.common.SnackbarEvent
import kotlinx.coroutines.launch

@Composable
fun SettingsSubScreenItem(
    text: String,
    switchState: Boolean, // the persisted state from ViewModel
    infoBoxText: String,
    showInfoBox: Boolean,
    onInfoBoxClick: (Boolean) -> Unit,
    onCheckedChangeConfirmed: (Boolean) -> Unit, // called only when snackbar confirmed
) {
    // Local temporary state for UI toggle
    var pendingSwitchState by remember { mutableStateOf(switchState) }

    LaunchedEffect(switchState) {
        pendingSwitchState = switchState
    }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text)

        IconButton(
            modifier = Modifier
                .size(36.dp)
                .padding(bottom = 16.dp),
            onClick = { onInfoBoxClick(!showInfoBox) }
        ) {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = null
            )
        }

        Spacer(
            modifier = Modifier
                .padding(end = 12.dp)
                .weight(.1f)
        )

        Switch(
            checked = pendingSwitchState,
            onCheckedChange = { newValue ->
                // update local UI state so switch thumb moves
                pendingSwitchState = newValue

                // show snackbar asking for confirmation
                scope.launch {
                    SnackbarController.sendEvent(
                        SnackbarEvent(
                            message = context.getString(R.string.snackbar_unsaved_changes),
                            action = SnackbarAction(
                                name = "Continue",
                                action = {
                                    // only commit when confirmed
                                    onCheckedChangeConfirmed(newValue)
                                }
                            )
                        )
                    )
                }
            }
        )
    }

    if (showInfoBox) {
        InfoBox(
            text = infoBoxText,
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
        )
    }
}
