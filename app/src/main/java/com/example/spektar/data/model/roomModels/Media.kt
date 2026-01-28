package com.example.spektar.data.model.roomModels

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.spektar.domain.model.SpecificMedia
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Media (
    @PrimaryKey
    val id_uuid : String,

    val name: String,
    val imageUrl: String,
    val description: String,
    val credits: String,
    val release_date: String,

    val daily_goal_set : Boolean,
    val dailyGoal : Int, // as in, amount you want to watch/read
    val totalSize : Int // as in, num of pages or episodes
)

fun Media.toSpecificMedia(): SpecificMedia = SpecificMedia(
    id_uuid = id_uuid,
    name = name,
    imageUrl = imageUrl,
    description = description,
    credits = credits,
    release_date = release_date
)