package com.example.spektar.domain.model.services

import com.example.spektar.data.model.edgeFunctionModels.EdgeResponse
import com.example.spektar.data.model.media.MediaLookup
import com.example.spektar.data.model.media.MediaPreview
import com.example.spektar.domain.model.SpecificMedia
import com.example.spektar.ui.mediaScreens.Category

interface MediaService {
    suspend fun fillCategory(accessToken: String, userId: String, categoryName: String): List<MediaPreview>?
    fun getAllCategories() : List<Category>

    suspend fun searchByName(name: String) : List<MediaPreview>

    suspend fun obtainCategoryWithMediaId(mediaId: String) : MediaLookup?
    suspend fun obtainDataByMediaId(partialMediaData: MediaPreview) : SpecificMedia
    suspend fun fetchTopMediaMatches(bearerToken: String, userId: String): EdgeResponse?
}