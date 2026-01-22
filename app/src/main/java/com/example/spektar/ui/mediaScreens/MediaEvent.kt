package com.example.spektar.ui.mediaScreens

import com.example.spektar.domain.media.MediaPreview

sealed interface MediaEvent {
    data class ObtainMediaById(val media: MediaPreview) : MediaEvent
    data class SearchForMedia(val name: String) : MediaEvent
}