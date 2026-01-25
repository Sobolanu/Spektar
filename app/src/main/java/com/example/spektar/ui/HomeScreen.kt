package com.example.spektar.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.example.spektar.ui.common.components.BottomBar

@Composable
fun HomeScreen(
    selectedIcon: Int,
    onBottomBarItemClick: (Int) -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomBar(
                selectedIcon = selectedIcon,
                onBottomBarItemClick = onBottomBarItemClick
            )
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        }
    }
}