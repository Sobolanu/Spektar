package com.example.spektar.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User (
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val avatar_url: String? = null, // or File?
    val accountCreationDate: String = ""
)