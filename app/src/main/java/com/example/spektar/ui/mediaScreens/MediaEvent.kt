package com.example.spektar.ui.mediaScreens

import com.example.spektar.data.model.media.MediaPreview

sealed interface MediaEvent {
    data class ObtainMediaById(val media: MediaPreview) : MediaEvent
    data class SearchForMedia(val name: String) : MediaEvent

    data class ReviewsForMedia(val mediaId: String) : MediaEvent
}