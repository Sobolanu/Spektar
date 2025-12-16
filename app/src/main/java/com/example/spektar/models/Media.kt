package com.example.spektar.models

/*
Defines a class Category (to describe a category of media, i.e books, movies, games...)
and a class SpecificMedia (used to refer to a specific piece of media, i.e "Avatar" or "CS:GO".
*/

open class Category (val mediaCategory : String)

data class SpecificMedia(
    val credits : String = "",
    val description : String = "",
    val imageUrl : String = "",
    val name : String = "",
    val releaseDate : String = ""
)