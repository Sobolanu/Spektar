package com.example.spektar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HobbiesScreen(
    navController : NavHostController,
    modifier : Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar() // loads function TopBar
        },
        bottomBar = {
            BottomBar() // loads function BottomBar
        }
    ) { paddingValues ->
        HobbiesScreenContent(
            navController,
            modifier = Modifier.padding(paddingValues),
            globalHobbyList
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HobbiesScreenContent(
    navController: NavHostController,
    modifier : Modifier = Modifier,
    hobbies : List<Hobby>
) {
    LazyColumn( // makes the whole screen a scrollable column
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var imageModifier : Modifier = Modifier
            .size(100.dp)
            .clickable(true, onClick = {
                navController.navigate(Details)
            })

        item(hobbies.size) {
            hobbies.forEachIndexed{index, hobby -> // loads each hobby
                hobby.LoadHobby(imageModifier)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar( // defines a topbar at top of screen
    modifier : Modifier = Modifier,
) {
    var iconButtonPressed : Boolean = false

    CenterAlignedTopAppBar(
        modifier = modifier,
        // there is a colors thing for color palette
        title = {
            Text(
                text = "Search",
                // color = SpektarTheme.colorScheme.onBackground.copy()
            )
        },

        actions = {
            IconButton(
                onClick = {iconButtonPressed = true} // figure out navigation to profile page
            ) {
                Icon(
                    imageVector = if(iconButtonPressed) {
                        topIcon.selectedIcon
                    } else {
                        topIcon.unselectedIcon
                    },

                    contentDescription = topIcon.title
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBar(
    modifier : Modifier = Modifier,
) {
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    NavigationBar {
        bottomIcons.forEachIndexed{index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    selectedItemIndex = index // figure out navigation to a home screen
                },

                label = {
                    Text(
                        text = item.title,
                        textAlign = TextAlign.Center
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