package com.example.spektar.ui.archiveScreen

import com.example.spektar.data.model.roomModels.Media
import com.example.spektar.domain.model.SpecificMedia

interface ArchiveEvent {
    data class saveMedia(val media: Media) : ArchiveEvent
    data class removeMedia(val media: Media) : ArchiveEvent
    data class mediaReview(val mediaId: String, val rating: Int, val message: String) : ArchiveEvent

    data class updateProgress(val newProgress: Int, val mediaId: String) : ArchiveEvent
}