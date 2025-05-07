package com.example.charts

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Producer(
    val id: String,
    val stage_name: String,
    val real_name: String,
    val current_label: String,
    val debut: LocalDate,
    val bio: String,
    val main_genre: String,
    val status: String,
    val country: String,
    val city: String,
    val profile_image: String
)