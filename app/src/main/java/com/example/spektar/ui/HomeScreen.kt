package com.example.spektar.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.spektar.R
import com.example.spektar.ui.common.components.BottomBar
import com.example.spektar.ui.common.components.navigationBarIcons.topProfileIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    goToProfile: () -> Unit,
    selectedIcon: Int,
    onBottomBarItemClick: (Int) -> Unit
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
        }
    ) { paddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                SplashScreen(
                    onTimeout = { }
                )

                Image(
                    painterResource(R.drawable.app_logo_transparent), // placeholder for now, add logo of the app
                    contentDescription = stringResource(R.string.logo_description)
                )
            }


        }
    }
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