package com.example.spektar.data.model.edgeFunctionModels

import kotlinx.serialization.Serializable

@Serializable
data class MatchResult(
    val media_id: String,
    val score: Double,
    val category: String
)