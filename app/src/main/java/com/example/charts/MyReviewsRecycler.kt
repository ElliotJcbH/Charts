package com.example.charts

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.datetime.DateTimePeriod
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class MyReviewsRecycler(
    private val data: List<Review>,
    private val reviewActionListener: OnReviewActionListener // Add this listener parameter
) : RecyclerView.Adapter<MyReviewsRecycler.ModelViewHolder>() {

    // Define the callback interface
    interface OnReviewActionListener {
        fun onReviewLongPress(reviewId: String)
        // Add other actions if needed, e.g., fun onReviewClick(reviewId: String)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ModelViewHolder {
        // Inflate the review card layoutå
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.inflatable_review_card, parent, false)
        return ModelViewHolder(view)
    }

    override fun getItemCount(): Int {
        // Return the size of the data list
        return data.size
    }

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {
        // Get the current review item
        val review = data[position]

        // Bind the data to the views
        Log.d("Review", review.toString())
        holder.bind(review)
    }

    inner class ModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // References to views in the layout
        private val albumName: TextView = itemView.findViewById(R.id.album_name)
        private val reviewerName: TextView = itemView.findViewById(R.id.reviewer_name)
        private val reviewDate: TextView = itemView.findViewById(R.id.review_date)
        private val reviewScore: RatingBar = itemView.findViewById(R.id.review_score)
        private val reviewTitle: TextView = itemView.findViewById(R.id.review_title)
        private val reviewContent: TextView = itemView.findViewById(R.id.review_content)
        private val favoriteLyric: TextView = itemView.findViewById(R.id.favorite_lyric)
        private val lyricLabel1: TextView = itemView.findViewById(R.id.lyric_label_1)
        private val worstLyric: TextView = itemView.findViewById(R.id.worst_lyric)
        private val lyricLabel2: TextView = itemView.findViewById(R.id.lyric_label_2)

        fun bind(review: Review) {

            albumName.text = review.album_name
            reviewerName.text = "by ${review.username}"

            // Format and set the date
            val parsedDateTime = OffsetDateTime.parse(review.date)
            reviewDate.text = parsedDateTime.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))

            // Set rating score
            reviewScore.rating = review.score ?: 0f

            // Set review title and content
            reviewTitle.text = review.title ?: ""
            reviewContent.text = review.content ?: ""

            // Set favorite and worst lyrics
            if(review.favorite_lyrics != "") {
                favoriteLyric.text = "\"${review.favorite_lyrics}\""
            } else {
                lyricLabel1.visibility = View.GONE
            }

            if(review.worst_lyrics != "") {
                worstLyric.text = "\"${review.worst_lyrics}\""
            } else {
                lyricLabel2.visibility = View.GONE
            }
        }
    }
}