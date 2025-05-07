package com.example.charts

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Home : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Apply window insets to the fragment's root view
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        return view    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val top10Albums = withContext(Dispatchers.IO) { // Perform network on IO thread
                    fetch_top_10()
                }

                withContext(Dispatchers.Main) {
                    if (top10Albums.isNotEmpty()) {
//                        statusTextView.text = "Fetched albums:\n" + top10Albums.joinToString("\n") { it.title }
                        Log.d("HomeFragment", "Successfully fetched top 10 albums: $top10Albums")

                        // If you have a RecyclerView in your fragment, update its adapter here
                    } else {
//                        statusTextView.text = "No top 10 albums found."
                        Log.d("HomeFragment", "Fetched empty list of top 10 albums.")
                    }
                }

            } catch (e: Exception) {
                // --- Handle Error on the Main Thread ---
                withContext(Dispatchers.Main) {
                    // Display an error message to the user
//                    statusTextView.text = "Error fetching albums: ${e.message}"
                    Log.e("HomeFragment", "Error fetching top 10 albums", e)
                    // You might also show a Toast or a Dialog
                }
            }

            try {
                val artist = withContext(Dispatchers.IO) {
                    fetch_full_artist_info("98c13327-681c-495e-b792-04f505fe0d25")
                }

                withContext(Dispatchers.Main) {
                    if(artist != null) {
                        Log.d("Successfully fetched artist", " ${artist}")
                    } else {
                        Log.d("That artist does not exist", "asdasd")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // Display an error message to the user
//                    statusTextView.text = "Error fetching albums: ${e.message}"
                    Log.e("HomeFragment", "Error fetching top 10 albums", e)
                    throw(e)
                    // You might also show a Toast or a Dialog
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Nullify view references to help prevent memory leaks
        // statusTextView = null // If using nullable vars
    }

}