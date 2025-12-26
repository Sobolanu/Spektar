package com.example.spektar.models.notes.room.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    val title : String = "", // title would be name of media this note belongs to
    val noteContent : String = "",

    @PrimaryKey val id: Int = 0
)