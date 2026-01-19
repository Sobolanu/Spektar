package com.example.spektar.data.model

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
@Entity
data class MediaId(
    @PrimaryKey
    val mediaId : String,
)

@Entity(tableName = "notes", foreignKeys = [
    ForeignKey(entity = MediaId::class, parentColumns = ["mediaId"], childColumns = ["mediaId"], onDelete = CASCADE, onUpdate = CASCADE)
])
data class Note(
    val mediaId: String, // what media this note belongs to

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // if this causes problems, autogenerate a random string ID
    val title : String,
    val text: String,
   // val textStyle: TextStyle
)

// implement textStyle later, you need a custom type converter from TextStyle to something like String.

/*
TextStyle(
        color = TODO(),
        fontSize = TODO(),
        fontWeight = TODO(),
        fontStyle = TODO(),
        fontSynthesis = TODO(),
        fontFamily = TODO(),
        baselineShift = TODO(), // tabs?
        localeList = TODO(),
        background = TODO(),
        textDecoration = TODO(),
        textAlign = TODO(),
        lineHeight = TODO(),
    )
 */