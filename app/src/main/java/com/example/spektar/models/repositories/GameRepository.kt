package com.example.spektar.models.repositories

import com.example.spektar.models.SpecificMedia
import com.example.spektar.supabase
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.json.Json

fun testSerialization() {
    val media = SpecificMedia( id = 1,
    name = "Test",
    imageUrl = "https://example.com",
    description = "desc",
    credits = "me",
    releaseDate = "2025-01-01" )
    val json =  Json.encodeToString(media)
    println(json)
}
class GameRepository() {
    suspend fun getAllGames(): List<SpecificMedia> {
        testSerialization()
        return supabase.from("games").select().decodeList<SpecificMedia>()
    }
}