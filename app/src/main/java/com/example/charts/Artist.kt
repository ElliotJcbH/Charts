package com.example.charts

import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Artist(
    val id: String,
    val stage_name: String,
    val real_name: String,
    val current_label: String,
    val debut: Date,
    val bio: String,
    val main_genre: String,
    val genre_name: String,
    val status: String,
    val country: String,
    val city: String,
    val is_group: Boolean,
    val members: Array<String>,
    val profile_image: String
)