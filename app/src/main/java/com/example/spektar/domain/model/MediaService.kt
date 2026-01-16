package com.example.spektar.domain.model

import com.example.spektar.data.model.EdgeResponse
import com.example.spektar.data.model.MediaPreview
import com.example.spektar.data.model.SpecificMedia

interface MediaService {
    suspend fun fillCategory(categoryName: String): List<MediaPreview>
    fun getAllCategories() : List<Category>
    suspend fun obtainDataByMediaId(mediaId: String) : SpecificMedia
    suspend fun fetchTopMediaMatches(edgeUrl: String, bearerToken: String, userId: String): EdgeResponse?
}