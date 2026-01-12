package com.example.spektar.data.model

import kotlinx.serialization.Serializable

/*
    Defines a class Category (to describe a category of media, i.e books, movies, games...)
    and a class SpecificMedia (used to refer to a specific piece of media, i.e "Avatar" or "CS:GO".
*/

@Serializable
data class SpecificMedia(
    val id: Long = 0,
    val name: String = "",
    val imageUrl: String = "",
    val description: String = "",
    val credits: String = "",
    val releaseDate: String = ""
)
