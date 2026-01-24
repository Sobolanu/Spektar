package com.example.spektar.ui.mediaScreens

import com.example.spektar.data.model.media.MediaPreview
import com.example.spektar.domain.model.SpecificMedia

data class MediaUiState (
    val medias: List<List<MediaPreview>?> = List(4, init = { emptyList() } ),
    val searchMedias: List<MediaPreview> = emptyList(),
    val categories: List<Category> = emptyList(),
    val media: SpecificMedia? = null,
)