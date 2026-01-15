package com.example.spektar.domain.usecase

import com.example.spektar.data.model.EdgeResponse
import com.example.spektar.data.model.IdPayload
import com.example.spektar.data.model.SpecificMedia
import com.example.spektar.data.repository.MediaRepository
import com.example.spektar.data.repository.globalCategoryList
import com.example.spektar.domain.model.Category
import com.example.spektar.domain.model.MediaService
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.serialization.json.Json

class MediaServiceImpl : MediaService {
    override suspend fun fillCategory(categoryName: String) : List<SpecificMedia> {
        return MediaRepository.getAllMediaInCategory(categoryName)
    }

    override fun obtainAllNamesInCategory(category: List<SpecificMedia>): List<String> {
        return category.map{ it.name }
    }

    override fun obtainAllImagesInCategory(category: List<SpecificMedia>): List<String> {
        return category.map{ it.imageUrl }
    }

    override fun getAllCategories(): List<Category> {
        return globalCategoryList
    }

    override suspend fun fetchTopMediaMatches(edgeUrl: String, bearerToken: String, userId: String): EdgeResponse? {
        val client = HttpClient(CIO)
        try {
            val payload = IdPayload(userId)
            val resp: HttpResponse = client.post(edgeUrl) {
                header(HttpHeaders.Authorization, "Bearer $bearerToken")

                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(payload))
                println("AFTER setBody, payload.id=${payload.id}")
            }

            val bodyText = resp.bodyAsText()
            return when (resp.status.value) {
                200 -> Json.decodeFromString<EdgeResponse>(bodyText)
                400 -> throw IllegalArgumentException("Bad request: $bodyText")
                401 -> throw SecurityException("Unauthorized: $bodyText")
                404 -> null
                else -> throw RuntimeException("Edge function error ${resp.status.value}: $bodyText")
            }
        } finally {
            client.close()
        }
    }
}