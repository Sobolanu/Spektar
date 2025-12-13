package com.example.spektar.models

/*
Repository of some placeholder categories
*/

class CategoryRepository {
    private val books = Category("Books")
    private val movies = Category("Movies")
    private val anime = Category("Anime")
    private val games = Category("Games")

    internal val globalCategoryList = mutableListOf(books, movies, anime, games)
    fun getCategories(): List<Category> = globalCategoryList
}