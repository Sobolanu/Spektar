package com.example.spektar.ui.mediaScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.spektar.ui.common.components.BottomBar
import com.example.spektar.ui.common.components.navigationBarIcons.topProfileIcon
import com.example.spektar.ui.common.modifiers.cardWithShadowModifier
import com.example.spektar.ui.navigation.graphs.categoryGraph.MediaDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreMedia(
    goToProfile: () -> Unit,
    onBottomBarItemClick : (Int) -> Unit,
    onImageClick: (MediaDetails) -> Unit,
    selectedIcon: Int,
    category: Category,
    state: MediaUiState
) { // grid screen, use LazyVerticalGrid.

    val scrollBehavior = enterAlwaysScrollBehavior()

    val index = when(category.mediaCategory) {
        "Shows" -> 0
        "Books" -> 1
        "Games" -> 2
        "Movies" -> 3
        else -> -1
    }

    if(index == -1) {
        IllegalArgumentException("Invalid category passed to screen MoreMedia.")
    }

    if(state.medias[index] == null) {
        IllegalArgumentException("Failed to retrieve media at screen MoreMedia.")
    }

    Scaffold(
        topBar = { MoreMediaPageTopBar(goToProfile, scrollBehavior = scrollBehavior) },
        bottomBar = { BottomBar(onBottomBarItemClick = onBottomBarItemClick, selectedIcon) },
        containerColor = category.categoryColor
    ) { paddingValues ->
        LazyVerticalGrid(
            modifier = Modifier.padding(paddingValues),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.Center,
            horizontalArrangement = Arrangement.Center,
        ) {
            items(state.medias[index]!!.size) { media ->
                Card(
                    onClick = { onImageClick(MediaDetails(partialMediaData = state.medias[index]!![media])) },

                    colors = CardColors( // sort card colors by category
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.tertiaryFixedDim,
                        disabledContentColor = MaterialTheme.colorScheme.onTertiaryFixed
                    ),

                    modifier = cardWithShadowModifier
                ){
                    AsyncImage(
                        model = state.medias[index]!![media].imageUrl,
                        contentDescription = state.medias[index]!![media].name,
                        modifier = Modifier
                            .size(175.dp)
                            .padding(horizontal = 8.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreMediaPageTopBar(
    goToProfile: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val iconButtonPressed by remember {mutableStateOf(false)}

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),

        title = { // you can add colors
            Text(
                text = "Search",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.onSecondaryFixed, RoundedCornerShape(4.dp))
                    .padding(4.dp)
            )
        },

        scrollBehavior = scrollBehavior,

        actions = { // profile icon basically
            IconButton(
                onClick = { goToProfile() } // figure out navigation to profile page
            ) {
                Icon(
                    imageVector = if (iconButtonPressed) {
                        topProfileIcon.selectedIcon
                    } else {
                        topProfileIcon.unselectedIcon
                    },

                    contentDescription = topProfileIcon.title
                )
            }
        }
    )
}