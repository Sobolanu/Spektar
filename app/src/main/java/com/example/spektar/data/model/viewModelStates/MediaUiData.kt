package com.example.spektar.data.model.viewModelStates

import com.example.spektar.domain.media.MediaPreview
import com.example.spektar.domain.model.Category

data class MediaUiData (
    val medias: List<List<MediaPreview>?> = emptyList(),
    val categories: List<Category> = emptyList(),
)