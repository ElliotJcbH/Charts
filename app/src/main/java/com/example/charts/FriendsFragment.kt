package com.example.charts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FriendsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var reviewsAdapter: MyReviewsRecycler
    private lateinit var emptyView: TextView

    private val reviewsList = mutableListOf<Review>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_friends, container, false)

        // Initialize RecyclerView
//        recyclerView = view.findViewById(R.id.reviews_recycler_view)
//        recyclerView.layoutManager = LinearLayoutManager(context)
//        emptyView = view.findViewById(R.id.empty_view)
//
//        val dividerItemDecoration = DividerItemDecoration(
//            recyclerView.context,
//            (recyclerView.layoutManager as LinearLayoutManager).orientation
//        )
//        recyclerView.addItemDecoration(dividerItemDecoration)
//
//
//        // Initialize the adapter with empty list (will be populated later)
//        reviewsAdapter = MyReviewsRecycler(reviewsList)
//        recyclerView.adapter = reviewsAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val myReviews = withContext(Dispatchers.IO) {
                    get_my_reviews()
                }

                withContext(Dispatchers.Main) {
                    if (myReviews.isNotEmpty()) {
                        Log.d("MyReviews", "Successfully fetched my reviews: $myReviews")

                        // Update the reviews list
                        reviewsList.clear()
                        reviewsList.addAll(myReviews)

                        // Notify the adapter that data has changed
                        reviewsAdapter.notifyDataSetChanged()


                        // Hide loading indicator if you have one
                        // loadingProgressBar.visibility = View.GONE
                    } else {
                        Log.d("MyReviews", "Fetched empty list of reviews.")
                        // You might want to show an empty state view here
                        // emptyStateView.visibility = View.VISIBLE
                        emptyView.visibility = View.VISIBLE
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // Display an error message to the user
                    Log.e("MyReviewsFragment", "Error fetching my reviews", e)

                    // You could show an error state here
                    // errorView.visibility = View.VISIBLE
                    // errorMessageTextView.text = "Error: ${e.message}"
                }
            }
        }
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyReviewsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}