package com.example.spektar.data.model.roomModels

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "notes", foreignKeys = [
    ForeignKey(
        entity = MediaId::class,
        parentColumns = ["mediaId"],
        childColumns = ["mediaId"],
        onDelete = ForeignKey.Companion.CASCADE,
        onUpdate = ForeignKey.Companion.CASCADE
    )
])
data class Note(
    val mediaId: String, // what media this note belongs to

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // if this causes problems, autogenerate a random string ID
    val title : String,
    val text: String,
   // val textStyle: TextStyle
)