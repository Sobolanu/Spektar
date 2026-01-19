package com.example.spektar.data.model.roomModels

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class MediaId(
    @PrimaryKey
    val mediaId : String,
)