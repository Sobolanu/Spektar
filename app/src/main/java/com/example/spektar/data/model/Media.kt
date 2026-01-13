package com.example.spektar.data.model

import com.example.spektar.data.model.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

/*
    Defines a class Category (to describe a category of media, i.e books, movies, games...)
    and a class SpecificMedia (used to refer to a specific piece of media, i.e "Avatar" or "CS:GO".
*/

@Serializable
data class SpecificMedia(
    @Serializable(with = UUIDSerializer::class)
    val id_uuid: UUID = UUID(0, 0),

    val name: String = "",
    val imageUrl: String = "",
    val description: String = "",
    val credits: String = "",
    val releaseDate: String = ""
)
