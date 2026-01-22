package com.example.spektar.ui.mediaScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.spektar.data.model.viewModelStates.MediaUiData
import com.example.spektar.domain.media.MediaPreview
import com.example.spektar.domain.model.Category
import com.example.spektar.ui.common.components.BottomBar
import com.example.spektar.ui.common.components.navigationBarIcons.topProfileIcon
import com.example.spektar.ui.common.modifiers.cardWithShadowModifier
import com.example.spektar.ui.common.modifiers.roundedCornerRow
import com.example.spektar.ui.navigation.routes.MediaDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable

/*
I think, at the end of the LoadCategoryText, there should be a button with "View completed"
where you can search up completed pieces of media (so, stuff you've watched/read)
 */

fun CategoryScreen(
    onEvent: (MediaEvent) -> Unit,
    goToProfile: () -> Unit,
    onImageClick: (MediaDetails) -> Unit,
    onMoreClick: (Category) -> Unit,
    onBottomBarItemClick: (Int) -> Unit,
    selectedIcon: Int,
    state: MediaUiData
) {
    val scrollBehavior = enterAlwaysScrollBehavior()
    var showSearchDialog by remember {mutableStateOf(false)}

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = { CategoryPageTopBar(onEvent = onEvent, goToProfile, scrollBehavior = scrollBehavior) },
        bottomBar = {
            BottomBar(
                onBottomBarItemClick = onBottomBarItemClick,
                selectedIcon,
            )
        },
    ) { paddingValues ->
        CategoryScreenContent(
            onImageClick = onImageClick,
            uiState = state,
            onMoreClick = onMoreClick,
            modifier = Modifier.padding(paddingValues),
        )

        // implement the ui for this
        /*
        if(showSearchDialog) {
            SearchOverlay(
                results = state.searchMedias,
                onDismiss = {
                    showSearchDialog = false
                },
                onSelect = { mediaPreview ->
                    onImageClick( MediaDetails(partialMediaData = mediaPreview) )
                }
            )
        }
         */
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreenContent(
    onImageClick: (MediaDetails) -> Unit,
    onMoreClick: (Category) -> Unit,
    uiState: MediaUiData,
    modifier: Modifier = Modifier,
) {
    val categories = uiState.categories
    LazyColumn( // LazyColumn loads only what is visible, scrollable is on by default
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface), // surface
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item(categories) {
            categories.forEachIndexed { index, category ->
                LoadCategoryText(category)
                LoadCategoryImages(
                    onImageClick = onImageClick,
                    onMoreClick = onMoreClick,
                    medias = uiState.medias[index]!!,
                    category = category
                )
            }
        }
    }
}

@Composable
fun LoadCategoryText(
    category : Category
) {
    Row( // spacing row for text alignment
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 16.dp,
                start = 24.dp
            )
    ) {
        Text(
            text = category.mediaCategory,
            fontSize = 32.sp,
            modifier = Modifier
                .padding(bottom = 16.dp)
        )
    }
}


@Composable
fun LoadCategoryImages(
    onImageClick: (MediaDetails) -> Unit,
    onMoreClick: (Category) -> Unit,
    medias: List<MediaPreview>,
    category: Category
) {
    LazyRow( // image row
        modifier = roundedCornerRow
            .background(category.categoryColor)

    // different colors based on different categories
    ) {
        items(medias, key = { it.imageUrl } ) { media ->
            Card( // if this doesn't work switch it to js normal card
                onClick = { //
                    onImageClick( MediaDetails(partialMediaData = media) )
                },

                colors = CardColors( // sort card colors by category
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.tertiaryFixedDim,
                    disabledContentColor = MaterialTheme.colorScheme.onTertiaryFixed
                ),

                modifier = cardWithShadowModifier
            ) {
                AsyncImage(
                    model = media.imageUrl,
                    contentDescription = media.name,
                    modifier = Modifier
                        .size(175.dp)
                        .padding(horizontal = 8.dp)
                )
            }
        }

        item {
            Card(
                onClick = { onMoreClick(category) },

                colors = CardColors( // sort card colors by category
                    containerColor = Color(0x64888888),
                    contentColor = Color(0x64888888),
                    disabledContainerColor = Color(0x64888888),
                    disabledContentColor = Color(0x64888888)
                ),

                modifier = cardWithShadowModifier
                    .height(175.dp)
                    .width(125.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        imageVector = Icons.Filled.AddCircleOutline,
                        contentDescription = "See more media",
                        modifier = Modifier.weight(1f)
                            .height(175.dp)
                            .width(125.dp)
                            .padding(horizontal = 8.dp)
                    )

                    Text(
                        text = "More",
                        color = Color(245, 241, 244),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // also add a sliding bar below this row
                // add "more" image that matches size and that
                // redirects to grid of images (as in, to more media)
            }
        }
    }

    Spacer(
        modifier = Modifier.height(36.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryPageTopBar(
    onEvent: (MediaEvent) -> Unit,
    goToProfile: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val iconButtonPressed by remember {mutableStateOf(false)}
    var text by remember { mutableStateOf("") }

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),

        /*
        Text(
                text = "Search",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.onSecondaryFixed, RoundedCornerShape(4.dp))
                    .padding(4.dp)
            )
         */
        title = { // you can add colors
            TextField(
                value = text,
                onValueChange = {
                    text = it
                    // maybe add slight delay delay(300)
                    onEvent(MediaEvent.SearchForMedia(text))
                },
                placeholder = {
                    Text("Search")
                }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchOverlay(
    results: List<MediaPreview>,
    onDismiss: () -> Unit,
    onSelect: (MediaPreview) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { onDismiss() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }

            LazyColumn {
                items(results) { item ->
                    ListItem(
                        headlineContent = { Text(item.name) },
                        supportingContent = { Text(item.id_uuid) }, // myb change
                        leadingContent = {
                            AsyncImage(model = item.imageUrl, contentDescription = null)
                        },
                        modifier = Modifier.clickable { onSelect(item) }
                    )
                }
            }
        }
    }
}
