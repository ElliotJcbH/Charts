package com.example.charts

import kotlinx.datetime.DateTimePeriod
import kotlinx.serialization.Serializable
import org.w3c.dom.Comment

@Serializable //hhahaha all this shit is nullable idk what the fuck im doing
data class Review (
    val id: String?,
    val user_id: String?,
    val user_name: String?,
    val album_id: String?,
    val title: String?,
    val content: String?,
    val date: DateTimePeriod?,
    val score: Float?,
    val likes: List<Review>?,
    val comments: List<Comment>?,
    val favorite_lyrics: String?,
    val favorite_song: String?,
    val worst_lyrics: String?,
    val worst_song: String?
)