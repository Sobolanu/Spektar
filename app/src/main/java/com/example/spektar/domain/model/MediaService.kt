package com.example.spektar.domain.model

import com.example.spektar.data.model.edgeFunctionModels.EdgeResponse
import com.example.spektar.domain.media.MediaLookup
import com.example.spektar.domain.media.MediaPreview
import com.example.spektar.domain.media.SpecificMedia

// DO NOT UNDER ANY CIRCUMSTANCES GET RID OF THE NON-EXPERIMENTAL FUNCTIONS
// WITHOUT THEM THE CODE BREAKS FOR SOME REASON AND I DON'T KNOW HOW TO FIX IT

interface MediaService {
    suspend fun fillCategory(categoryName: String): List<MediaPreview>
    suspend fun EXPERIMENTALfillCategory(accessToken: String, userId: String, categoryName: String): List<MediaPreview>?
    fun getAllCategories() : List<Category>

    suspend fun obtainCategoryWithMediaId(mediaId: String) : MediaLookup?
    suspend fun obtainDataByMediaId(partialMediaData: MediaPreview) : SpecificMedia
    suspend fun fetchTopMediaMatches(bearerToken: String, userId: String): EdgeResponse?
}