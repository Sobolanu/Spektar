package com.example.spektar.ui.userAuthScreens.states

import java.io.File

data class SignUpState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    var avatar : File? = null
)