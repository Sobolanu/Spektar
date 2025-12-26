package com.example.spektar.models

class User(
    val id : Int,

    val name : String,
    val password : String,
    val email: String,
    // val image : ImageVector (or some other type idrk), implement this once you migrate to supabase
    // as you cannot store images in firebase because cloud storage costs FILTHY DIRTY MONEY.
    val bio : String,
    val dateOfBirth: String,
) {
    fun createUser() {

    }

    fun modifyUserAttribute() { // i.e change name, change password, bio etc. etc.

    }

    fun deleteUser() {

    }
}