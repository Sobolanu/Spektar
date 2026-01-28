package com.example.spektar.ui.archiveScreen

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.spektar.data.model.media.MediaPreview
import com.example.spektar.domain.model.SpecificMedia
import com.example.spektar.ui.common.components.BottomBar
import com.example.spektar.ui.common.components.navigationBarIcons.topProfileIcon
import com.example.spektar.ui.common.modifiers.cardWithShadowModifier
import com.example.spektar.ui.navigation.graphs.categoryGraph.MediaDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchiveScreen(
    goToProfile: () -> Unit,
    onBottomBarItemClick : (Int) -> Unit,
    onImageClick: (MediaDetails) -> Unit,
    selectedIcon: Int,
    state: List<SpecificMedia>?,
) {
    val scrollBehavior = enterAlwaysScrollBehavior()

    Scaffold(
        topBar = { ArchivePageTopBar(goToProfile, scrollBehavior = scrollBehavior) },
        bottomBar = { BottomBar(onBottomBarItemClick = onBottomBarItemClick, selectedIcon) },
    ) { paddingValues ->
        LazyVerticalGrid(
            modifier = Modifier.padding(paddingValues),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.Center,
            horizontalArrangement = Arrangement.Center,
        ) {
            if(state.isNullOrEmpty()) {
                item {
                    Text("Add some media to this archive!")
                }
            } else {
                items(state.size) { media ->
                    Card(
                        onClick = { onImageClick(MediaDetails(partialMediaData = MediaPreview(
                            state[media].id_uuid,
                            state[media].imageUrl,
                            state[media].name)))
                        },

                        colors = CardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                            disabledContainerColor = MaterialTheme.colorScheme.tertiaryFixedDim,
                            disabledContentColor = MaterialTheme.colorScheme.onTertiaryFixed
                        ),

                        modifier = cardWithShadowModifier
                    ){
                        AsyncImage(
                            model = state[media].imageUrl,
                            contentDescription = state[media].name,
                            modifier = Modifier
                                .size(175.dp)
                                .padding(horizontal = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchivePageTopBar(
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
                text = "Archived media",
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

                    contentDescription = stringResource(topProfileIcon.title)
                )
            }
        }
    )
}