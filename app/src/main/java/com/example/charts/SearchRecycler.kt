package com.example.charts

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.datetime.DateTimePeriod
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToLong
import coil.load
import kotlinx.serialization.json.Json

class SearchRecycler(private val data: List<AlbumInfo>) : RecyclerView.Adapter<SearchRecycler.ModelViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ModelViewHolder {
        // Inflate the album card layout
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.inflatable_album_card_search, parent, false)

        return ModelViewHolder(view)
    }

    override fun getItemCount(): Int {
        // Return the size of the data list
        return data.size
    }

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {
        // Get the current album item
        val album = data[position]

        // Bind the data to the views
        Log.d("album", album.toString())
        holder.bind(album)
    }

    inner class ModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // References to views in the layout
        private val albumName: TextView = itemView.findViewById(R.id.album_name)
        private val artistName: TextView = itemView.findViewById(R.id.artist_name)
        private val albumCover: ImageView = itemView.findViewById(R.id.album_cover)
        private val reviewCount: TextView = itemView.findViewById(R.id.review_count)
        private val score: TextView = itemView.findViewById(R.id.score)
        private val card: ConstraintLayout = itemView.findViewById(R.id.card)

        fun bind(album: AlbumInfo) {

            card.setOnClickListener {
                val albumJSON = Json.encodeToString(AlbumInfo.serializer(), album)
                val intent = Intent(itemView.context, AlbumActivity::class.java)
//                intent.putExtra("albumId", album.id.toString())
                intent.putExtra("albumInfo", albumJSON)

                itemView.context.startActivity(intent)
            }

            albumName.text = album.title
            artistName.text = album.artist_name
//            albumCover.text = album.album_cover
            if(album.album_cover != "") {
                albumCover.load(album.album_cover)
            }
//            albumCover.load("https://plus.unsplash.com/premium_photo-1694540892449-5c3170caf81c?q=80&w=3464&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D") {
//
//            }

            score.text = String.format("%.1f", album.average_score ?: 0.0)
            reviewCount.text = "${album.review_count} reviews"

        }
    }
}