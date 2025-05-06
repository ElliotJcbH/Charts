package com.example.charts

import kotlinx.serialization.Serializable
import java.text.DateFormat
import java.util.Date

@Serializable
data class AlbumInfo(
    val id: String,
    val artist_id: String,
    val artist_name: String,
    val artist_image: String,
    val producer_name: String,
    val producer_image: String,
    val producer_id: String,
    val label: String,
    val songs: Array<String>,
    val link: String,
    val genre_names: Array<String>,
    val description: Array<String>,
    val release_date: DateFormat,
    val explicit: Boolean,
    val language: String,
    val country: String,
    val copyright: String,
    val awards: Array<String>,
    val title: String,
    val album_length: Int,
    val album_cover: String,
    val review_count: Int,
    val average_score: Float
)

//class Album {
//
//
//}