package com.example.spektar.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.spektar.models.topBackArrowIcon
import com.example.spektar.models.topProfileIcon
import com.example.spektar.viewmodels.MediaUiState
import com.example.spektar.viewmodels.MediaViewModel

@Composable
fun MediaDetailsScreen(
    onBackClick: () -> Unit = {},
    mediaID : Int,
    viewModel : MediaViewModel = viewModel(),
    modifier : Modifier = Modifier,
) {
    val medias by viewModel.uiState.observeAsState() // figure out a better name for this val

    DetailsScreenContent(
         onBackClick,
         medias!!.medias[mediaID].name,
         medias!!.medias[mediaID].imageUrl,
         medias!!.medias[mediaID].description
     )
}

@Composable
fun DetailsScreenContent(
    onBackClick: () -> Unit = {},
    mediaName : String,
    mediaImageURL : String,
    mediaDescription : String,
    modifier : Modifier = Modifier
) {
    Scaffold(
        topBar = { DetailsPageTopBar(onBackClick) },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),

            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                AsyncImage(
                    modifier = Modifier
                        .size(200.dp),

                    model = mediaImageURL,
                    contentDescription = null
                )
            }

            // review thingamajig

            item {
                Text(
                    modifier = Modifier
                        .padding(vertical = 24.dp),
                    text = mediaName,
                    autoSize = TextAutoSize.StepBased(
                        16.sp,
                        38.sp,
                        2.sp
                    ),

                    lineHeight = 46.sp,
                    textAlign = TextAlign.Center
                )
            }

            item {
                Text(
                    mediaDescription,
                    textAlign = TextAlign.Center
                )
            }

            // add notes item (item that leads to a notes page for THIS SPECIFIC PIECE of media)
            // notes item should be a local database!!!
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun DetailsPageTopBar(
    onBackClick: () -> Unit = {},
    modifier : Modifier = Modifier
) {
    var backButtonPressed : Boolean = false
    var profileIconPressed : Boolean = false

    TopAppBar(
        modifier = modifier,
        title = {Text("test")},

        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = if(backButtonPressed) {
                        topBackArrowIcon.selectedIcon
                    } else {
                        topBackArrowIcon.unselectedIcon
                    },

                    contentDescription = topBackArrowIcon.title
                )
            }
        },

        actions = {
            IconButton(
                onClick = { profileIconPressed = true } // figure out navigation to profile page
            ) {
                Icon(
                    imageVector = if(profileIconPressed) {
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