package com.example.spektar.ui.navigation.graphs.categoryGraph

import com.example.spektar.data.model.media.MediaPreview
import com.example.spektar.ui.mediaScreens.Category
import kotlinx.serialization.Serializable

@Serializable data object Media

@Serializable data class NoteScreen(val id: String)
@Serializable data class MediaDetails(val partialMediaData: MediaPreview)
@Serializable data class MoreMedia (val category : Category)
@Serializable object CategoryScreen
