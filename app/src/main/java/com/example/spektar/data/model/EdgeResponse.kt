package com.example.spektar.data.model

import kotlinx.serialization.Serializable

/*
    buckets.shows,
    buckets.books,
    buckets.games,
    buckets.movies
 */
@Serializable
data class EdgeResponse(val results: List<List<MatchResult>>)