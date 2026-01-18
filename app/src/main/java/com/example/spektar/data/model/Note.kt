package com.example.spektar.data.model

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.serialization.json.Json
@Entity
data class Note(
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