package com.example.charts

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.UUID

class AlbumActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_album)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val albumId = intent.getStringExtra("album_id") // Assuming you pass the ID as a String extra
//        val albumTitleTextView: TextView = findViewById(R.id.albumTitle)

        if (albumId != null) {
            // Use lifecycleScope to launch coroutines in an Activity
            lifecycleScope.launch {
                // Fetch Album Info (assuming you have a function like fetch_album_by_id)
                try {
                    val albumInfo = withContext(Dispatchers.IO) {
                        var album: AlbumInfo = fetch_album(albumId)
                        album
                    }

                    // Update UI on the Main Thread with Album Info
                    withContext(Dispatchers.Main) {
//                        albumTitleTextView.text = albumInfo.title // Update the album title TextView
                        // Update other album info UI elements here
                        Log.d("AlbumActivity", "Fetched album info: ${albumInfo.title}")
                    }

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
//                        albumTitleTextView.text = "Error loading album info"
                        Log.e("AlbumActivity", "Error fetching album info", e)
                    }
                }

                // Fetch Reviews for this Album
                try {
                    val reviews = withContext(Dispatchers.IO) {
                        fetch_reviews_for_album(albumId) // Call your RPC function
                    }

                    // Update UI on the Main Thread with Reviews
                    withContext(Dispatchers.Main) {
                        if (reviews.isNotEmpty()) {
//                            reviewsStatusTextView.text = "Reviews:\n" + reviews.joinToString("\n") { "${it.score}/10 - ${it.title}" }
                            // If you have a RecyclerView for reviews, update its adapter here
                            Log.d("AlbumActivity", "Fetched ${reviews.size} reviews.")
                        } else {
//                            reviewsStatusTextView.text = "No reviews yet."
                            Log.d("AlbumActivity", "No reviews found for this album.")
                        }
                    }

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
//                        reviewsStatusTextView.text = "Error loading reviews: ${e.message}"
                        Log.e("AlbumActivity", "Error fetching reviews", e)
                    }
                }
            }
        } else {
            // Handle the case where albumId is null (e.g., show an error message)
//            albumTitleTextView.text = "Invalid Album"
//            reviewsStatusTextView.text = "Cannot load reviews due to invalid album ID."
        }
    }

}