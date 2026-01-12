package com.example.spektar.data.repository

import androidx.compose.ui.graphics.Color
import com.example.spektar.domain.model.Category

class CategoryRepository {
    private val books = Category("Books", Color(0xFFFF5D60))
    private val shows = Category("Shows", Color(0xFF06783F))
    private val games = Category("Games", Color(0xFFEFD07A))
    private val movies = Category("Movies", Color(0xFF063F78))

    private val globalCategoryList = mutableListOf(shows, books, games, movies)
    fun getAllCategories(): List<Category> = globalCategoryList
}