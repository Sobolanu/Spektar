package com.example.spektar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentDataType.Companion.Text
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil.compose.AsyncImage
import com.example.spektar.ui.theme.SpektarTheme
import kotlinx.serialization.Serializable
import org.jetbrains.annotations.Async

open class Hobby ( // custom hobbies would need to inherit from this, that's why it's marked with open
    val HobbyName : String // should this nullable?
){
    @Composable
    internal fun LoadHobby(imageModifier: Modifier) {
        LoadHobbyText()
        LoadHobbyImages(imageModifier)
    }
    @Composable
    internal fun LoadHobbyText() {
        Row( // spacing row for text alignment
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = HobbyName, // when we figure out color palette, make this look better
                fontSize = 32.sp,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )
        }
    }

    @Composable
    internal fun LoadHobbyImages(imageModifier : Modifier) {
        LazyRow( // image row
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            item(globalHobbyImagesList.size) {
                globalHobbyImagesList.forEach { image ->
                    AsyncImage( // more images, let's load 6 per more then 7th will be "More..."
                        model = image,
                        contentDescription = null,
                        modifier = imageModifier
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(98.dp)
        )
    }
}