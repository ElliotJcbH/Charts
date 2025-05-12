package com.example.charts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var reviewsAdapter: SearchRecycler
    private lateinit var emptyView: TextView
    private lateinit var searchBar: EditText // Declare searchBar here

    private val albumList = mutableListOf<AlbumInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val initialQuery = intent.getStringExtra("query")
        if (initialQuery.isNullOrBlank()) { // Use isNullOrBlank for cleaner check
            finish()
            return // Finish and exit onCreate if no initial query
        }

        recyclerView = findViewById(R.id.search_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)
        emptyView = findViewById(R.id.empty_view)

        reviewsAdapter = SearchRecycler(albumList)
        recyclerView.adapter = reviewsAdapter

        searchBar = findViewById(R.id.searchBar) // Initialize searchBar
        searchBar.setText(initialQuery) // Set initial query in search bar

        // Set the editor action listener for the search bar
        searchBar.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
                val query = searchBar.text.toString()
                if (query.isNotBlank()) {
                    performSearch(query) // Call the reusable function
                } else {
                    // Handle blank search query after typing
                    albumList.clear()
                    reviewsAdapter.notifyDataSetChanged()
                    emptyView.text = "Please enter a search term."
                    emptyView.visibility = View.VISIBLE
                }
                true // Indicate that the action is handled
            } else {
                false // Let the system handle other actions
            }
        }

        // Perform the initial search when the activity is created
        performSearch(initialQuery)
    }

    // Reusable function to perform the album search and update the UI
    private fun performSearch(query: String) {
        lifecycleScope.launch {
            try {
                // Show loading indicator if you have one
                // loadingProgressBar.visibility = View.VISIBLE
                emptyView.visibility = View.GONE // Hide empty view while searching

                val albums = withContext(Dispatchers.IO) {
                    find_albums(query) // Perform search on IO thread
                }

                withContext(Dispatchers.Main) { // Switch back to Main for UI updates
                    albumList.clear()
                    albumList.addAll(albums)

                    if (albums.isNotEmpty()) {
                        Log.d("SearchActivity", "Successfully fetched albums for search: $albums")
                        reviewsAdapter.notifyDataSetChanged()
                        emptyView.visibility = View.GONE // Hide empty view if results are found
                    } else {
                        Log.d("SearchActivity", "Fetched empty list for search.")
                        reviewsAdapter.notifyDataSetChanged() // Still notify to clear old items
                        emptyView.text = "No albums found for your search." // Optional: customize empty message
                        emptyView.visibility = View.VISIBLE // Show empty view
                    }
                    // Hide loading indicator
                    // loadingProgressBar.visibility = View.GONE
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { // Handle error on Main thread
                    Log.e("SearchActivity", "Error during album search", e)
                    albumList.clear() // Clear list on error
                    reviewsAdapter.notifyDataSetChanged() // Update UI to show empty
                    emptyView.text = "An error occurred during search." // Show error message
                    emptyView.visibility = View.VISIBLE
                    // Hide loading indicator
                    // loadingProgressBar.visibility = View.GONE
                }
            }
        }
    }
}