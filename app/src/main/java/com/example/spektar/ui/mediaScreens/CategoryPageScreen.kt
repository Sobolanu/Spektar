package com.example.spektar.ui.mediaScreens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.spektar.R
import com.example.spektar.data.model.media.MediaPreview
import com.example.spektar.ui.common.components.BottomBar
import com.example.spektar.ui.common.components.navigationBarIcons.topProfileIcon
import com.example.spektar.ui.common.modifiers.cardWithShadowModifier
import com.example.spektar.ui.common.modifiers.roundedCornerRow
import com.example.spektar.ui.navigation.graphs.categoryGraph.MediaDetails

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun CategoryScreen(
    onEvent: (MediaEvent) -> Unit,
    goToProfile: () -> Unit,
    onImageClick: (MediaDetails) -> Unit,
    onMoreClick: (Category) -> Unit,
    onBottomBarItemClick: (Int) -> Unit,
    selectedIcon: Int,
    state: MediaUiState
) {
    val scrollBehavior = enterAlwaysScrollBehavior()
    var showSearchDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CategoryPageTopBar(
                onEvent = onEvent,
                goToProfile = goToProfile,
                scrollBehavior = scrollBehavior,
                onSearch = { showSearchDialog = true }
            )
        },
        bottomBar = {
            BottomBar(onBottomBarItemClick = onBottomBarItemClick, selectedIcon = selectedIcon)
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
        ) {
            CategoryScreenContent(
                onImageClick = onImageClick,
                uiState = state,
                onMoreClick = onMoreClick,
                modifier = Modifier.fillMaxSize()
            )

            AnimatedSearchOverlay(
                visible = showSearchDialog,
                results = state.searchMedias,
                onDismiss = { showSearchDialog = false },
                onSelect = { mediaPreview ->
                    showSearchDialog = false
                    onImageClick(MediaDetails(partialMediaData = mediaPreview))
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreenContent(
    onImageClick: (MediaDetails) -> Unit,
    onMoreClick: (Category) -> Unit,
    uiState: MediaUiState,
    modifier: Modifier = Modifier,
) {
    val categories = uiState.categories
    LazyColumn( // LazyColumn loads only what is visible, scrollable is on by default
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface), // surface
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
                    onImageClick(MediaDetails(partialMediaData = media))
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
                        modifier = Modifier
                            .weight(1f)
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
    scrollBehavior: TopAppBarScrollBehavior,
    onSearch: () -> Unit,
) {
    val iconButtonPressed by remember {mutableStateOf(false)}
    var text by remember { mutableStateOf("") }

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),

        title = { // you can add colors
            TextField(
                value = text,
                placeholder =  { Text(stringResource(R.string.search)) },
                onValueChange = {
                    text = it

                    onEvent(MediaEvent.SearchForMedia(text))
                    onSearch()
                },
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedSearchOverlay(
    visible: Boolean,
    results: List<MediaPreview>,
    onDismiss: () -> Unit,
    onSelect: (MediaPreview) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrimColor = Color.Black.copy(alpha = 0.5f)

    // root full-screen container only when visible (keeps it out of layout when hidden)
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically(initialOffsetY = { -it / 2 }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { -it / 2 }),
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(scrimColor)
                .clickable(
                    // consume clicks on scrim and dismiss
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    // hide keyboard and clear focus before dismissing
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    onDismiss()
                }
        ) {
            // overlay content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .align(Alignment.TopCenter)
                    .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                    .padding(vertical = 8.dp)
                    .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { /* consume */ }
                    .imePadding() // allow content to move with keyboard
            ) {
                // back button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    IconButton(onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        onDismiss()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Search results", style = MaterialTheme.typography.titleMedium)
                }

                // results list
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 420.dp)
                ) {
                    items(results) { item ->
                        ListItem(
                            headlineContent = { Text(item.name) },
                            supportingContent = { Text(item.id_uuid) },
                            leadingContent = {
                                AsyncImage(
                                    model = item.imageUrl,
                                    contentDescription = item.name,
                                    modifier = Modifier.size(64.dp)
                                )
                            },
                            modifier = Modifier
                                .clickable {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                    onSelect(item)
                                }
                                .padding(horizontal = 8.dp)
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}