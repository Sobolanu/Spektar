package com.example.spektar.models.repositories

import androidx.compose.ui.graphics.Color
import com.example.spektar.models.Category

class CategoryRepository {
    private val books = Category("Books", Color(0xFFFF5D60))
    private val shows = Category("Shows", Color(0xFF06783F))
    private val games = Category("Games", Color(0xFFEFD07A))
    private val movies = Category("Movies", Color(0xFF063F78))

    internal val globalCategoryList = mutableListOf(shows, books, games, movies)
    fun getAllCategories(): List<Category> = globalCategoryList
}