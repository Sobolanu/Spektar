package com.example.spektar.screens.AppBars

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
import com.example.spektar.models.navigationIcons.bottomIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBar(
    onBottomBarItemClick: ( Int ) -> Unit
) {
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(1)
    }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.tertiaryContainer, // tertiary
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
    ) {
        bottomIcons.forEachIndexed{ index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    // figure out how to maintain selected icon state from screen navigation
                    // later, right now even if you press "Settings" Home will still be selected so yeah
                    selectedItemIndex = index
                    onBottomBarItemClick( index )
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
                }
            )
        }
    }
}