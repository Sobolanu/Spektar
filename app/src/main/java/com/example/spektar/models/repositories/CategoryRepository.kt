package com.example.spektar.models.repositories

import com.example.spektar.models.Category

class CategoryRepository {
    private val books = Category("Books")
    private val shows = Category("Shows")
    private val games = Category("Games")
    private val movies = Category("Movies")

    internal val globalCategoryList = mutableListOf(shows, books, games, movies)
    fun getAllCategories(): List<Category> = globalCategoryList
}