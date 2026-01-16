package com.example.spektar.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MediaPreview(
    val id_uuid : String,
    val name : String,
    val imageUrl : String
)