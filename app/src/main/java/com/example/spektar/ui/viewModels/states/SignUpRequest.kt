package com.example.spektar.ui.viewModels.states

import java.io.File

data class SignUpRequest(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    var avatar : File? = null
)