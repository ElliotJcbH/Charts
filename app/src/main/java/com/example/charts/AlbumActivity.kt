package com.example.charts

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
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

        val albumJSON = intent.getStringExtra("albumInfo") // Assuming you pass the ID  as a String extra
        val albumInfoFromJSON = albumJSON?.let { Json.decodeFromString(AlbumInfo.serializer(), it) }
        val reviewButton: AppCompatButton = findViewById(R.id.review_button)

        lifecycleScope.launch {
            try {
                val review = does_review_exist(MyUserObject.myUserId, albumInfoFromJSON?.id.toString())

                if(review?.id != null || review?.id != "") {
                    val stringifiedReview = review?.let { Json.encodeToString(Review.serializer(), it) }
                    reviewButton.text = "Update Review"
                    reviewButton.setOnClickListener{
                        val reviewBottomSheet = DialogReviewBottomSheet.newInstance(albumJSON.toString())
                        reviewBottomSheet.show(supportFragmentManager, DialogReviewBottomSheet.TAG)
                    }
//                    reviewButton.setOnClickListener{
//                        val reviewBottomSheet = DialogReviewBottomSheet.newExistingInstance(albumJSON.toString(), stringifiedReview.toString())
//                        reviewBottomSheet.show(supportFragmentManager, DialogReviewBottomSheet.TAG)
//                    }
                } else {
                    Log.d("AlbumActivity", "Review does not exist")
                    reviewButton.text = "Review"
                    reviewButton.setOnClickListener{
                        val reviewBottomSheet = DialogReviewBottomSheet.newInstance(albumJSON.toString())
                        reviewBottomSheet.show(supportFragmentManager, DialogReviewBottomSheet.TAG)
                    }
                }
            } catch (e: Exception) {
                Log.e("AlbumActivity", e.toString())
            }
        }

        if (albumInfoFromJSON != null) {
            // Use lifecycleScope to launch coroutines in an Activity
            lifecycleScope.launch {
                // Fetch Album Info (assuming you have a function like fetch_album_by_id)
                try {
                    val albumInfo = withContext(Dispatchers.IO) {
                        var album: AlbumInfo = fetch_album(albumInfoFromJSON.id.toString())
                    }

                    // Update UI on the Main Thread with Album Info
                    withContext(Dispatchers.Main) {
//                        albumTitleTextView.text = albumInfo.title // Update the album title TextView
                        // Update other album info UI elements here
                        Log.d("AlbumActivity", "Fetched album info: ${albumInfoFromJSON.title}")
                    }

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e("AlbumActivity", "Error fetching album info", e)
                    }
                }

                try {
                    val reviews = withContext(Dispatchers.IO) {
                        fetch_reviews_for_album(albumInfoFromJSON.id.toString()) // Call your RPC function
                    }

                    withContext(Dispatchers.Main) {
                        if (reviews.isNotEmpty()) {
                            // If you have a RecyclerView for reviews, update its adapter here
                            Log.d("AlbumActivity", "Fetched ${reviews.size} reviews.")
                        } else {
//                            reviewsStatusTextView.text = "No reviews yet."
                            Log.d("AlbumActivity", "No reviews found for this album.")
                        }
                    }

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e("AlbumActivity", "Error fetching reviews", e)
                    }
                }
            }
        }
    }

}