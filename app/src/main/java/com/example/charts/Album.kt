package com.example.charts

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.DateFormat
import java.time.format.DateTimeFormatter
import java.util.Date
//
//object PostgresDateSerializer : KSerializer<LocalDate> {
//    private val formatter = DateTimeFormatter.ISO_DATE // ISO 8601 format (YYYY-MM-DD)
//
//    override val descriptor: SerialDescriptor =
//        PrimitiveSerialDescriptor("PostgresDate", PrimitiveKind.STRING)
//
////    override fun serialize(encoder: Encoder, value: LocalDate) {
////        encoder.encodeString(value.format(formatter))
////    }
//
//    override fun serialize(encoder: Encoder, value: LocalDate) {
//        encoder.encodeString(formatter.format(value))
//    }
//    override fun deserialize(decoder: Decoder): LocalDate {
//        return LocalDate.parse(decoder.decodeString(), formatter)
//    }
//}

@Serializable
data class AlbumInfo(
    val id: String?,
    val artist_id: String?,
    val artist_name: String?,
    val artist_image: String?,
    val producer_name: String?,
    val producer_image: String?,
    val producer_id: String?,
    val label: String?,
    val songs: List<String>?,
    val link: String?,
    val genre_names: List<String>?,
    val description: String?,
//    @Serializable(with = PostgresDateSerializer::class)
    val release_date: String?,
    val explicit: Boolean?,
    val language: String?,
    val country: String?,
    val copyright: String?,
    val awards: List<String>?,
    val title: String? = "",
    val album_length: Int?,
    val album_cover: String?,
    val review_count: Int?,
    val average_score: Float?
)

//class Album {
//
//
//}