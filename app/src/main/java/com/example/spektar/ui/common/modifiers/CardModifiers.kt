package com.example.spektar.ui.common.modifiers

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

val cardWithShadowModifier = Modifier.padding(16.dp)
    .dropShadow(
        shape = RoundedCornerShape(20.dp),
        shadow = Shadow(
            radius = 10.dp,
            spread = 3.dp,
            color = Color(0x95000000),
            offset = DpOffset(x = 2.dp, 2.dp)
        )
    )