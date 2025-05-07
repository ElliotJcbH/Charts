package com.example.charts

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.w3c.dom.Comment

@Serializable
data class User( // review encompasses everything
    val id: String?,
    val email: String?,
    val username: String?
)