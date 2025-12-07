package com.example.spektar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

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