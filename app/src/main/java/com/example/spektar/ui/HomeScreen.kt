package com.example.spektar.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.spektar.R
import com.example.spektar.ui.common.components.BottomBar
import com.example.spektar.ui.common.components.navigationBarIcons.topProfileIcon
import com.example.spektar.data.model.roomModels.Media

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    goToQuestionnaireScreen: () -> Unit,
    goToProfile: () -> Unit,
    goToArchiveScreen: () -> Unit,
    selectedIcon: Int,
    onBottomBarItemClick: (Int) -> Unit,
    state: List<Media>
) {
    val scrollBehavior = enterAlwaysScrollBehavior()

    Scaffold(
        topBar =  {
            HomePageTopBar(
                goToProfile = goToProfile,
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            BottomBar(
                selectedIcon = selectedIcon,
                onBottomBarItemClick = onBottomBarItemClick
            )
        },
        contentWindowInsets = WindowInsets(left = 8.dp, right = 8.dp)
    ) { paddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                Image(
                    painterResource(R.drawable.app_logo_transparent),
                    contentDescription = stringResource(R.string.logo_description)
                )

                Spacer(modifier = Modifier.padding(bottom = 32.dp))
            }

            if(state.isEmpty()) {
                item {
                    Text(
                        "Save a few medias to populate the home screen!",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            } else {
                items(state) {
                    HomePageMedia(it)
                }
            }


            item{
                Button(
                    onClick = { goToArchiveScreen() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Text(
                        "Go to archive",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            item {
                Button(
                    onClick = { goToQuestionnaireScreen() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Text(
                        stringResource(R.string.fill_out_questionnaire),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun HomePageMedia(
    media: Media
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = media.name,
            style = MaterialTheme.typography.headlineSmall,
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        AsyncImage(
            model = media.imageUrl,
            modifier = Modifier
                .size(100.dp)
                .padding(end = 8.dp)
            ,
            contentDescription = media.name
        )

        Spacer(modifier = Modifier.weight(1f))

        CircularProgressIndicator(
            progress = { 0.3f },
            modifier = Modifier.size(80.dp)
        )
    }

    Spacer(modifier = Modifier.padding(bottom = 16.dp))
    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePageTopBar(
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

        title = {
            Text(
                text = "Home",
                style = MaterialTheme.typography.titleLarge
            )
        },

        scrollBehavior = scrollBehavior,

        actions = {
            IconButton(
                onClick = { goToProfile() }
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