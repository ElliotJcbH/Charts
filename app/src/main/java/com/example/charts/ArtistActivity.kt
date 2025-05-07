package com.example.charts

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArtistActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_artist)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val artistId = intent.getStringExtra("artist_id")

        if(artistId != null) {

            lifecycleScope.launch {
                try {
                    val artistInfo = withContext(Dispatchers.IO) {
                        val artist: Artist = fetch_full_artist_info(artistId)
                        artist
                    }

                    withContext(Dispatchers.Main) {
//                        albumTitleTextView.text = albumInfo.title // Update the album title TextView
                        // Update other album info UI elements here
                        Log.d("AlbumActivity", "Fetched album info: ${artistInfo.stage_name}")
                    }
                } catch (e: Exception) {
                    Log.d("Error in artist activity: ", e.message.toString())
                    throw(e)
                }

                try {
                    val getDisco = withContext(Dispatchers.IO) {
                        val disco: List<AlbumInfo> = fetch_discography(artistId)
                    }
                } catch (e: Exception) {
                    Log.d("Error in artist activity:", e.message.toString())
                    throw(e)
                }

            }

        } else {
            Log.d("No string was passed", "No string passsed")
        }

    }
}