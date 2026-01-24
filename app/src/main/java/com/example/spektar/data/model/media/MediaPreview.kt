package com.example.spektar.data.model.media

import kotlinx.serialization.Serializable

@Serializable
data class MediaPreview(
    val id_uuid: String,
    val imageUrl : String,
    val name : String
)