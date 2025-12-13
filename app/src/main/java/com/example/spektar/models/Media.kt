package com.example.spektar.models

/*
Defines a class Category (to describe a category of media, i.e books, movies, games...)
and a class SpecificMedia (used to refer to a specific piece of media, i.e "Avatar" or "CS:GO".
*/

open class Category (val mediaCategory : String)

data class SpecificMedia(
    val category : String,
    val specificMediaName: String,
    val mediaImageURL : String,
    val specificMediaDescription : String, // would also contain tags like "Horror, Action"
    val credits : String, // eventually make this into a list
    val releaseDate : String
) : Category(category) {
}