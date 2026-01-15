package com.example.spektar.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String = "",
    val username: String = "",
    val avatar_url: String = ""
)