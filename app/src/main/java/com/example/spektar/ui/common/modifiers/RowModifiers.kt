package com.example.spektar.ui.common.modifiers

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

val roundedCornerRow = Modifier.fillMaxWidth()
    .padding(horizontal = 8.dp)
    .clip(shape = RoundedCornerShape(15.dp))