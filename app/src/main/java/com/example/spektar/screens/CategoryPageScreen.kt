package com.example.spektar.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.spektar.MediaDetails
import com.example.spektar.models.Category
import com.example.spektar.models.SpecificMedia
import com.example.spektar.models.bottomIcons
import com.example.spektar.models.topProfileIcon
import com.example.spektar.viewmodels.MediaViewModel

/*
Follow this layout:
    Books - red
    Shows - green
    Movies - blue
    Games - yellow

Implement your material3 style to this
*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun CategoryScreen(
    onImageClick: (MediaDetails) -> Unit = {},
    viewModel : MediaViewModel = viewModel(),
    modifier : Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = { CategoryPageTopBar(scrollBehavior = scrollBehavior) },
        bottomBar = { BottomBar() },
    ) { paddingValues ->
        CategoryScreenContent(
            onImageClick = onImageClick,
            categories = uiState.categories,
            medias = uiState.medias,
            modifier = Modifier.padding(paddingValues),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreenContent(
    onImageClick: (MediaDetails) -> Unit = {},
    categories : List<Category>,
    medias : List<List<SpecificMedia>>,
    modifier: Modifier = Modifier,
) {
    LazyColumn( // LazyColumn loads only what is visible, scrollable is on by default
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        item(categories) {
            categories.forEachIndexed {index, category ->
                LoadCategory(
                    onImageClick = onImageClick,
                    category = category,
                    indexOfCategory = index,
                    urls = medias[index].map { it.imageUrl },
                )
            }
        }
    }
}

@Composable
fun LoadCategory(
    onImageClick: (MediaDetails) -> Unit = {},
    category: Category,
    indexOfCategory: Int,
    urls: List<String>,
) {
    LoadCategoryText(category)
    LoadCategoryImages(
        onImageClick = onImageClick,
        indexOfCategory = indexOfCategory,
        listOfUrls = urls
    )
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
            text = category.mediaCategory, // when we figure out color palette, make this look better
            fontSize = 32.sp,
            modifier = Modifier
                .padding(bottom = 16.dp)
        )
    }
}


@Composable
fun LoadCategoryImages(
    onImageClick: (MediaDetails) -> Unit = {},
    indexOfCategory: Int,
    listOfUrls: List<String>,
) {
    LazyRow( // image row, similar principle to LazyColumn
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .background(MaterialTheme.colorScheme.inversePrimary)
    ) {
        item {
            for(i in 0..<listOfUrls.size) {
                Card(
                    onClick = { onImageClick(MediaDetails(indexOfCategory, i)) },

                    colors = CardColors(
                        containerColor = MaterialTheme.colorScheme.inverseSurface,
                        contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        disabledContentColor = MaterialTheme.colorScheme.onSurface
                    ),

                    shape = CardDefaults.elevatedShape,
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 48.dp,
                        pressedElevation = 16.dp
                    ),
                    modifier = Modifier.padding(16.dp)
                ) {
                    AsyncImage(
                        model = listOfUrls[i],
                        contentDescription = null,
                        modifier = Modifier
                            .size(175.dp)
                            .padding(horizontal = 8.dp)
                    )
                }
            }
            // add "more" image that matches size and that
            // redirects to grid of images (as in, to more media)
        }
    }

    Spacer(
        modifier = Modifier.height(36.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryPageTopBar(
    scrollBehavior: TopAppBarScrollBehavior
) { // defines a topbar at top of screen
    val iconButtonPressed = false

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),

        title = { // you can add colors
            Text(
                text = "Search",
                style = MaterialTheme.typography.titleLarge
            )
        },

        scrollBehavior = scrollBehavior,

        actions = { // profile icon basically
            IconButton(
                onClick = {} // figure out navigation to profile page
            ) {
                Icon(
                    imageVector = if(iconButtonPressed) {
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
fun BottomBar() {
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        bottomIcons.forEachIndexed{ index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    selectedItemIndex = index // figure out navigation to screens
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