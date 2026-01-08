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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun SettingsSubScreenItem(
    text: String,
    switchState: Boolean,

    infoBoxText: String,
    showInfoBox: Boolean,

    onInfoBoxClick: (Boolean) -> Unit,
    onCheckedChange: (Boolean) -> Unit,
) {
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
            checked = switchState,
            onCheckedChange = { newValue ->
                onCheckedChange(newValue)
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