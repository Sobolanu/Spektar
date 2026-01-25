package com.example.spektar.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        TriangleWithLight()
    }

    // Optional: navigate after delay
    LaunchedEffect(Unit) {
        delay(3000) // 3 seconds
        onTimeout()
    }
}

@Composable
fun TriangleOutline(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(120.dp)) {
        val path = Path().apply {
            moveTo(size.width / 2, 0f)          // top
            lineTo(0f, size.height)             // bottom left
            lineTo(size.width, size.height)     // bottom right
            close()
        }

        // Draw only the outline (stroke), no fill
        drawPath(
            path = path,
            color = Color.White,
            style = Stroke(width = 6f) // thickness of border
        )
    }
}


@Composable
fun LightRay() {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        )
    )

    // make this better
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawLine(
            color = Color.White.copy(alpha = alpha),
            start = Offset(0f,2f + 0.0868f * size.width),
            end = Offset(size.width / 2, size.height / 2),
            strokeWidth = 8f
        )
    }
}

@Composable
fun RainbowRefraction() {
    val colors = listOf(Color.Red, Color.Yellow, Color.Green, Color.Blue)

    Canvas(modifier = Modifier.fillMaxSize()) {
        val start = Offset(size.width / 2, size.height / 2)
        val spread = 40f

        colors.forEachIndexed { index, color ->
            val end = Offset(size.width - 50f, size.height / 2 + (index - colors.size / 2) * spread)
            drawLine(color, start, end, strokeWidth = 6f)
        }
    }
}

@Composable
fun TriangleWithLight() {
    Box(contentAlignment = Alignment.Center) {
        TriangleOutline()
        LightRay()
        RainbowRefraction()
    }
}
