package com.example.spektar.ui.mediaScreens

import com.example.spektar.data.model.media.MediaPreview

data class MediaUiData (
    val medias: List<List<MediaPreview>?> = emptyList(),
    val searchMedias: List<MediaPreview> = emptyList(),
    val categories: List<Category> = emptyList(),
)