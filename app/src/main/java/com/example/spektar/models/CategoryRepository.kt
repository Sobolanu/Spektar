package com.example.spektar.models

/*
Repository of some placeholder categories
*/

class CategoryRepository { // if this is gonna become Firebase-d, categoryRepository would be just a repository of all collection names
    private val books = Category("Books")
    private val shows = Category("Shows")
    private val games = Category("Games")
    // private val movies = Category("Movies")

    internal val globalCategoryList = mutableListOf(shows, books, games /* */)
    fun getAllCategories(): List<Category> = globalCategoryList
}