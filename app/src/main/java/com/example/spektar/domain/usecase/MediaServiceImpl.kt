package com.example.spektar.domain.usecase

import com.example.spektar.data.model.EdgeResponse
import com.example.spektar.data.model.IdPayload
import com.example.spektar.data.model.media.MediaLookup
import com.example.spektar.data.model.media.MediaPreview
import com.example.spektar.data.model.media.SpecificMedia
import com.example.spektar.data.remote.SupabaseClientProvider
import com.example.spektar.data.repository.MediaRepository
import com.example.spektar.data.repository.globalCategoryList
import com.example.spektar.domain.model.Category
import com.example.spektar.domain.model.MediaService
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
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

    // DO NOT UNDER ANY CIRCUMSTANCES GET RID OF THE NON-EXPERIMENTAL FUNCTIONS
    // WITHOUT THEM THE CODE BREAKS FOR SOME REASON AND I DON'T KNOW HOW TO FIX IT
    override suspend fun fillCategory(categoryName: String) : List<MediaPreview> {
        return MediaRepository.getAllMediaInCategory(categoryName)
    }

    override suspend fun EXPERIMENTALfillCategory(
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
            val mediaData = MediaRepository.EXPERIMENTALgetAllMediaInCategory(categoryName, mediaIds)

            mediaData.forEachIndexed { index, preview ->
                preview.copy(id_uuid = mediaIds[index])
            }

            return mediaData
        }

        Exception("Failed to process data")
        return null
    }

    /*
    override suspend fun processData(
        session: UserSession?,
        userId: String
    ) {
        val recommendedMedia : EdgeResponse? = if(session != null) { // adapt so this fetches all media data?
            fetchTopMediaMatches(
                edgeUrl = "https://rlyotyktmhyflfyljpmr.supabase.co/functions/v1/content-recommendation",
                bearerToken = session.accessToken,
                userId = userId
            )
        } else {
            Exception("fetchMedia failed to receive a session.")
            null
        }
    } */

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

    /*
        the idea with this function is to obtain the data by the media ID, by first querying
        the category the mediaId is in (through val lookup = ...), then it queries the media data.
        Given this function runs in MediaDetails, you already have (id_uuid, name, imageUrl) from
        CategoryPageScreen, therefore it'd be pointless to query that data again. Instead - we pass
        it into this function, and only query missing data (description, credits, release_date).
     */
    override suspend fun obtainDataByMediaId(partialMediaData : MediaPreview) : SpecificMedia {
        val lookup = obtainCategoryWithMediaId(partialMediaData.id_uuid)

        if(lookup != null) {
            val media = SupabaseClientProvider.db.from(lookup.category)
                .select(Columns.list("description", "credits", "release_date")) {
                    filter {
                        eq("id_uuid", partialMediaData.id_uuid)
                    }
                }
                .decodeSingleOrNull<SpecificMedia>()

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

    override suspend fun fetchTopMediaMatches(bearerToken: String, userId: String): EdgeResponse? {
        val client = HttpClient(CIO)
        try {
            val payload = IdPayload(userId)
            val resp: HttpResponse = client.post("https://rlyotyktmhyflfyljpmr.supabase.co/functions/v1/content-recommendation") {
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