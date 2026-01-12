package com.example.spektar.ui.common.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import com.example.spektar.domain.model.navigationBarIcons.bottomIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBar(
    onBottomBarItemClick: ( Int ) -> Unit,
    selectedIcon: Int,
) {
    var selectedItemIndex by rememberSaveable { mutableStateOf(selectedIcon) }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.tertiaryContainer, // tertiary
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
    ) {
        bottomIcons.forEachIndexed{ index, item ->
            NavigationBarItem(
                selected = (selectedItemIndex == index),
                onClick = {
                    selectedItemIndex = index
                    onBottomBarItemClick(index) // responsible for changing the selectedIcon
                },

                label = {
                    Text(
                        text = item.title,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelLarge
                    )
                },

                icon = {
                    Icon(
                        imageVector = if(index == selectedItemIndex) {
                            item.selectedIcon
                        } else item.unselectedIcon,
                        contentDescription = item.title
                    )
                },
            )
        }
    }
}