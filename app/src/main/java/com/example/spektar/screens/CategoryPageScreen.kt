package com.example.spektar.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.spektar.models.bottomIcons
import com.example.spektar.models.topProfileIcon
import com.example.spektar.viewmodels.MediaViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun CategoryScreen(
    onImageClick: (Int) -> Unit = {},
    viewModel : MediaViewModel = viewModel(),
    modifier : Modifier = Modifier
) {
    val media by viewModel.uiState.observeAsState()

    Scaffold(
        modifier = modifier,
        topBar = {CategoryPageTopBar()},
        bottomBar = {BottomBar()}
    ) { paddingValues ->
        CategoryScreenContent(
            onImageClick = onImageClick,
            categories = media!!.categoryNames,
            media!!.mediaImageURLs,
            modifier = Modifier.padding(paddingValues),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreenContent(
    onImageClick: (Int) -> Unit = {},
    categories: List<String>,
    listOfUrls: List<String>,
    modifier: Modifier = Modifier,
) {
    LazyColumn( // LazyColumn loads only what is visible, scrollable is on by default
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        items(categories) { category ->
            LoadCategory(
                onImageClick = onImageClick,
                category = category,
                urls = listOfUrls,
            )
        }
    }
}

@Composable
fun LoadCategory(
    onImageClick: (Int) -> Unit = {},
    category: String,
    urls: List<String>,
) {
    LoadCategoryText(category)
    LoadCategoryImages(onImageClick = onImageClick, listOfUrls = urls)
}

@Composable
fun LoadCategoryText(
    category : String?
) {
    Row( // spacing row for text alignment
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = category ?: "DNE", // when we figure out color palette, make this look better
            fontSize = 32.sp,
            modifier = Modifier
                .padding(bottom = 16.dp)
        )
    }
}


@Composable
fun LoadCategoryImages(
    onImageClick: (Int) -> Unit = {},
    listOfUrls: List<String>,
) {
    LazyRow( // image row, similar principle to LazyColumn
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {

        item() {
            listOfUrls.forEachIndexed{index, url ->
                AsyncImage(
                    model = url, // add an image with a + icon for like "more" i guess?
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clickable{onImageClick(index)}
                )
            }
        }
    }

    Spacer(
        modifier = Modifier.height(98.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryPageTopBar(modifier : Modifier = Modifier) { // defines a topbar at top of screen
    var iconButtonPressed : Boolean = false

    CenterAlignedTopAppBar(
        modifier = modifier,
        title = { // you can add colors
            Text(text = "Search")
        },

        actions = {
            IconButton(
                onClick = {iconButtonPressed = true} // figure out navigation to profile page
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
fun BottomBar(
    modifier : Modifier = Modifier,
) {
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    NavigationBar {
        bottomIcons.forEachIndexed{ index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    selectedItemIndex = index // figure out navigation to screens
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