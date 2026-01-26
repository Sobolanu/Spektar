package com.example.spektar.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.spektar.ui.common.components.BottomBar

@Composable
fun QuestionaireScreen(
    onBottomBarItemClick: (Int) -> Unit,
    selectedIcon : Int,
) {
    Scaffold(
        bottomBar = { BottomBar(onBottomBarItemClick, selectedIcon )}
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {

            }

            item {

            }
        }
    }
}