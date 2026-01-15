package com.example.spektar.domain.model

import com.example.spektar.data.model.EdgeResponse
import com.example.spektar.data.model.SpecificMedia

interface MediaService {
    suspend fun fillCategory(categoryName: String): List<SpecificMedia>
    fun obtainAllNamesInCategory(category: List<SpecificMedia>) : List<String>
    fun obtainAllImagesInCategory(category: List<SpecificMedia>) : List<String>
    fun getAllCategories() : List<Category>
    suspend fun fetchTopMediaMatches(edgeUrl: String, bearerToken: String, userId: String): EdgeResponse?
}