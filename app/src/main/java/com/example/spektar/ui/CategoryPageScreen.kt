package com.example.spektar.ui

import androidx.compose.foundation.background
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.spektar.domain.model.Category
import com.example.spektar.domain.model.navigationBarIcons.topProfileIcon
import com.example.spektar.ui.common.components.BottomBar
import com.example.spektar.ui.common.modifiers.cardWithShadowModifier
import com.example.spektar.ui.common.modifiers.roundedCornerRow
import com.example.spektar.ui.navigation.routes.MediaDetails
import com.example.spektar.ui.viewModels.MediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable

/*
I think, at the end of the LoadCategoryText, there should be a button with "View completed"
where you can search up completed pieces of media (so, stuff you've watched/read)

modify your viewmodel you dingus
remember, the ViewModel should be responsible for loading and rendering stuff!!!
 */

fun CategoryScreen(
    onImageClick: (MediaDetails) -> Unit,
    onBottomBarItemClick: (Int) -> Unit,
    selectedIcon: Int,
    viewModel : MediaViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = { CategoryPageTopBar(scrollBehavior = scrollBehavior) },
        bottomBar = { BottomBar(
            onBottomBarItemClick = onBottomBarItemClick,
            selectedIcon,
        ) },
    ) { paddingValues ->
        CategoryScreenContent(
            onImageClick = onImageClick,
            categories = uiState.categories,
            viewModel = viewModel,
            modifier = Modifier.padding(paddingValues),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreenContent(
    onImageClick: (MediaDetails) -> Unit,
    categories : List<Category>,
    viewModel : MediaViewModel,
    modifier: Modifier = Modifier,
) {
    LazyColumn( // LazyColumn loads only what is visible, scrollable is on by default
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface), // surface
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        item(categories) {
            categories.forEachIndexed {index, category ->
                LoadCategoryText(category)
                LoadCategoryImages(
                    onImageClick = onImageClick,
                    indexOfCategory = index,
                    listOfUrls = viewModel.obtainAllImagesInCategory(index),
                    mediaNames = viewModel.obtainAllNamesInCategory(index),
                    categoryColor = category.categoryColor
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
    indexOfCategory: Int,
    listOfUrls: List<String>,
    mediaNames: List<String>,
    categoryColor : Color
) {
    LazyRow( // image row
        modifier = roundedCornerRow.background(categoryColor) // different colors based on different categories
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

                    modifier = cardWithShadowModifier
                ) {
                    AsyncImage(
                        model = listOfUrls[i],
                        contentDescription = mediaNames[i],
                        modifier = Modifier
                            .size(175.dp)
                            .padding(horizontal = 8.dp)
                    )
                }
            }

            // also add a sliding bar below this row
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