package com.example.spektar.domain.repository

import com.example.spektar.data.model.media.MediaPreview

sealed interface MediaEvent {
    data class ObtainMediaById(val media: MediaPreview) : MediaEvent
}