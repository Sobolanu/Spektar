package com.example.spektar.screens.mediaCategories

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.spektar.MediaDetails
import com.example.spektar.models.Category
import com.example.spektar.models.SpecificMedia
import com.example.spektar.models.navigationIcons.bottomIcons
import com.example.spektar.models.navigationIcons.topProfileIcon
import com.example.spektar.screens.AppBars.BottomBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable

/*
I think, at the end of the LoadCategoryText, there should be a button with "View completed"
where you can search up completed pieces of media (so, stuff you've watched/read)
 */

fun CategoryScreen(
    onImageClick: (MediaDetails) -> Unit,
    onBottomBarItemClick: (Int) -> Unit,
    viewModel : MediaViewModel,
    modifier : Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = { CategoryPageTopBar(scrollBehavior = scrollBehavior) },
        bottomBar = { BottomBar( onBottomBarItemClick ) },
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
    onImageClick: (MediaDetails) -> Unit,
    categories : List<Category>,
    medias : List<List<SpecificMedia>>,
    modifier: Modifier = Modifier,
) {
    LazyColumn( // LazyColumn loads only what is visible, scrollable is on by default
        modifier = modifier.fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface), // surface
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
    onImageClick: (MediaDetails) -> Unit,
    category: Category,
    indexOfCategory: Int,
    urls: List<String>,
) {
    LoadCategoryText(category)
    LoadCategoryImages(
        onImageClick = onImageClick,
        indexOfCategory = indexOfCategory,
        listOfUrls = urls,
        categoryColor = category.categoryColor
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
    indexOfCategory: Int,
    listOfUrls: List<String>,
    categoryColor : Color
) {
    LazyRow( // image row, similar principle to LazyColumn
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clip(shape = RoundedCornerShape(15.dp))
            .background(categoryColor) // different colors based on different categories
    ) {
        item {
            for(i in 0..<listOfUrls.size) {
                Card(
                    onClick = { onImageClick(MediaDetails(indexOfCategory, i)) },

                    colors = CardColors( // sort card colors by category
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.tertiaryFixedDim,
                        disabledContentColor = MaterialTheme.colorScheme.onTertiaryFixed
                    ),

                    modifier = Modifier.padding(16.dp)
                        .dropShadow(
                            shape = RoundedCornerShape(20.dp),
                            shadow = Shadow(
                                radius = 10.dp,
                                spread = 3.dp,
                                color = Color(0x95000000),
                                offset = DpOffset(x = 2.dp, 2.dp)
                            )
                        )
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
) {
    val iconButtonPressed = false

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer, // secondary
            titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),

        title = { // you can add colors
            Box() { // wrap a rounded corner rectangle around the "Search" text
                Text(
                    text = "Search",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },

        scrollBehavior = scrollBehavior,

        actions = { // profile icon basically
            IconButton(
                onClick = {} // figure out navigation to profile page
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