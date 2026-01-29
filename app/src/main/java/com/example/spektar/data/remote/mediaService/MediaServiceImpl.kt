package com.example.spektar.data.remote.mediaService

import com.example.spektar.data.model.edgeFunctionModels.EdgeResponse
import com.example.spektar.data.model.edgeFunctionModels.IdPayload
import com.example.spektar.data.model.media.MediaLookup
import com.example.spektar.data.model.media.MediaPreview
import com.example.spektar.data.remote.SupabaseClientProvider
import com.example.spektar.domain.model.SpecificMedia
import com.example.spektar.data.repository.MediaRepository
import com.example.spektar.data.repository.globalCategoryList
import com.example.spektar.ui.mediaScreens.Category
import com.example.spektar.domain.model.services.MediaService
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.filter.TextSearchType
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
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.collections.mapOf

// used in fun searchByName, same stuff as MediaPreview but i don't want to break anything.
@Serializable
data class MediaLookupRow(
    val id_uuid: String,
    val image_url: String,
    val media_name: String
)


@Serializable
data class RatingInsert(
    val user_id: String,
    val media_id: String,
    val rating: Int,
    val review_text: String?,
    val media_type: String
)

@Serializable
data class FullMediaData(
    val id_uuid : String = "",
    val name: String = "",
    val imageUrl: String = "",
    val description: String = "",
    val credits: String = "",
    val release_date: String = "",
    val average_rating: Float = 0.0f,
    val rating_count: Int = 0
)

@Serializable
data class ReviewData(
    val rating : Int = 0,
    val review_text: String = "",
    val username: String = ""
)

class MediaServiceImpl : MediaService {
    override suspend fun searchByName(name: String): List<MediaPreview> {
        val data = SupabaseClientProvider.db
            .from("media_lookup")
            .select(Columns.list("id_uuid", "image_url", "media_name")) {
                filter {
                    textSearch("media_name", name, TextSearchType.PHRASETO)
                }
            }
            .decodeList<MediaLookupRow>()

        return data.map { MediaPreview(it.id_uuid, it.image_url, it.media_name) }
    }


    override suspend fun fillCategory(
        accessToken: String,
        userId: String,
        categoryName: String,
    ) : List<MediaPreview>? {
        val recommendedMedia : EdgeResponse? = // adapt so this fetches all media data?
            fetchTopMediaMatches(
                bearerToken = accessToken,
                userId = userId
            )

        val categoryIndex = when (categoryName) {
            "shows" -> 0
            "books" -> 1
            "games" -> 2
            "movies" -> 3
            else -> -1 // invalid category
        }

        if(recommendedMedia != null && categoryIndex != -1) {
            val mediaIds = recommendedMedia.results[categoryIndex].map { it.media_id }
            val mediaData = MediaRepository.getAllMediaInCategory(categoryName, mediaIds)

            mediaData.forEachIndexed { index, preview ->
                preview.copy(id_uuid = mediaIds[index])
            }

            return mediaData
        }

        Exception("Failed to process data")
        return null
    }

    override fun getAllCategories(): List<Category> {
        return globalCategoryList
    }

    override suspend fun obtainCategoryWithMediaId(mediaId: String): MediaLookup? {
        return SupabaseClientProvider.db.from("media_lookup")
            .select {
                filter { eq("id_uuid", mediaId) }
            }
            .decodeSingleOrNull<MediaLookup>()
    }

    // used to query only important data and nothing else
    override suspend fun obtainDataByMediaId(partialMediaData : MediaPreview) : FullMediaData {
        val lookup = obtainCategoryWithMediaId(partialMediaData.id_uuid)

        if(lookup != null) {
            val media = SupabaseClientProvider.db.from(lookup.category)
                .select(Columns.list("description", "credits", "release_date", "average_rating", "rating_count")) {
                    filter {
                        eq("id_uuid", partialMediaData.id_uuid)
                    }
                }
                .decodeSingleOrNull<FullMediaData>()

            if(media != null) {
                return media.copy(
                    id_uuid = partialMediaData.id_uuid,
                    name = partialMediaData.name,
                    imageUrl = partialMediaData.imageUrl
                )
            } else {
                throw Exception("Failed to retrieve media")
            }
        }

        throw IllegalArgumentException("Invalid mediaId passed to function obtainDataByMediaId")
    }

    override suspend fun leaveReview(userId: String, mediaId: String, review: Int, message: String) {
        val category = obtainCategoryWithMediaId(mediaId)

        if(category != null) {
            val payload = RatingInsert(
                user_id = userId,
                media_id = mediaId,
                rating = review,
                review_text = message,
                media_type = category.category.dropLast(1)
            )

            SupabaseClientProvider.db.from("ratings")
                .insert (payload)
        }
    }

    override suspend fun fetchReviews(mediaId: String) : List<ReviewData> {
        val reviews = SupabaseClientProvider.db.from("ratings")
            .select(Columns.list("rating", "review_text", "username")) {
                filter {
                    eq("media_id", mediaId)
                }
            }.decodeList<ReviewData>()

        return reviews
    }

    override suspend fun fetchTopMediaMatches(bearerToken: String, userId: String): EdgeResponse? {
        val client = HttpClient(CIO)
        try {
            val payload = IdPayload(userId)
            val resp: HttpResponse = client.post("https://rlyotyktmhyflfyljpmr.supabase.co/functions/v1/content-recommendation") {
                header(HttpHeaders.Authorization, "Bearer $bearerToken")

                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(payload))
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