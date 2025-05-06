package com.example.charts

import kotlinx.datetime.DateTimePeriod
import kotlinx.serialization.Serializable
import org.w3c.dom.Comment

@Serializable
data class Friend( // review encompasses everything
    val id: String,
    val user_id: String,
    val album_id: String,
    val title: String,
    val content: String,
    val date: DateTimePeriod, // the review date
    val likes: Array<String>,
    val comments: Array<Comment>,
    val favorite_lyrics: String,
    val favorite_song: String,
    val worst_lyrics: String,
    val worst_song: String,
    val profile_image: String,
    val username: String,
    val achievemets: String,
    val flair: String
)