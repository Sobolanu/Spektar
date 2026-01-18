package com.example.spektar.data.model.viewModelStates

import java.io.File

data class UserSignUpData(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    var avatar : File? = null
)