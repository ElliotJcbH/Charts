package com.example.charts

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import coil.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class DialogReviewBottomSheet : BottomSheetDialogFragment() {

    private var userId = MyUserObject.myUserId
    private var albumJSON: String = ""
    private var existingReview: String? = null
    private val jsonParser = Json { ignoreUnknownKeys = true }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Retrieve the arguments from the Bundle using unique keys
        arguments?.let {
            albumJSON = it.getString(ARG_ALBUM_INFO) ?: ""
            existingReview = it.getString(ARG_EXISTING_REVIEW)
        }
        // Add logs here to confirm the values after retrieving from arguments
        Log.d(TAG, "onCreate: albumJSON = $albumJSON")
        Log.d(TAG, "onCreate: existingReview = $existingReview")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.bottom_sheet_review, container, false)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        // These logs were helpful for debugging, but you can remove them if you like
        // Log.d("AlbumActivity", existingReview.toString())
        // Log.d("AlbumActivity", albumJSON.toString())

        // Deserialize albumJSON into AlbumInfo - this should now be the correct JSON
        val albumInfo = jsonParser.decodeFromString(AlbumInfo.serializer(), albumJSON)

        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog

            // Get the internal FrameLayout of the bottom sheet dialog
            val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.accent_gray))

            val albumName: TextView? = bottomSheetDialog.findViewById(R.id.album_name)
            albumName?.text = albumInfo.title ?: ""

            val postButton: AppCompatButton? = bottomSheetDialog.findViewById(R.id.post_button)
            val titleInput: AppCompatEditText? = bottomSheetDialog.findViewById(R.id.title_input)
            val reviewInput: AppCompatEditText? = bottomSheetDialog.findViewById(R.id.review_input)
            val favLyricInput: AppCompatEditText? = bottomSheetDialog.findViewById(R.id.favorite_lyric_input)
            val worstLyricInput: AppCompatEditText? = bottomSheetDialog.findViewById(R.id.worst_lyric_input)
            val scoreInput: RatingBar? = bottomSheetDialog.findViewById(R.id.scoreRatingBar)
            val spinner: Spinner? = bottomSheetDialog.findViewById(R.id.fav_song_spinner)
            val spinner2: Spinner? = bottomSheetDialog.findViewById(R.id.worst_song_spinner)
            val progressBar: ProgressBar? = bottomSheetDialog.findViewById(R.id.buttonProgressBar)
            val cover: ImageView? = bottomSheetDialog.findViewById(R.id.album_cover)

            if(albumInfo.album_cover != null) {
                cover?.load(albumInfo.album_cover)
            }

            var score: Float = 0.0f
            var favSong: String = ""
            var worstSong: String = ""

            // spinner adapters
            val mutableSongsList: MutableList<String> = albumInfo.songs?.toMutableList() ?: mutableListOf<String>()
            mutableSongsList.add(0, "None")

            val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mutableSongsList)
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner?.adapter = spinnerAdapter
            spinner?.setSelection(0)

            val spinnerAdapter2 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mutableSongsList)
            spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner2?.adapter = spinnerAdapter // Note: This should probably be spinnerAdapter2
            spinner2?.setSelection(0)

            spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val selectedItem = parent.getItemAtPosition(position).toString()
                    favSong = selectedItem
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Do nothing here, or handle the case where nothing is selected
                }
            }

            spinner2?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val selectedItem = parent.getItemAtPosition(position).toString()
                    worstSong = selectedItem
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Do nothing here, or handle the case where nothing is selected
                }
            }

            postButton?.isEnabled = false
            postButton?.setBackgroundDrawable(resources.getDrawable(R.drawable.inactive_button, null))
            postButton?.setTextColor(resources.getColor(R.color.text, null))
            scoreInput?.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                score = rating
                postButton?.isEnabled = true
                postButton?.setBackgroundDrawable(resources.getDrawable(R.drawable.register_page_button2, null))
                postButton?.setTextColor(resources.getColor(R.color.accent_gray, null))

            }


            if(existingReview != null && existingReview!!.isNotBlank()) { // Added null and blank check
                try {
                    Log.d(TAG, "Attempting to decode existingReview: $existingReview") // Log the string
                    val prevData = jsonParser.decodeFromString(Review.serializer(), existingReview!!)
                    Log.d(TAG, "Decoded existingReview successfully: $prevData") // Log the decoded object

                    // Safely apply data, checking for nulls
                    prevData?.let { data ->
                        if (data.score != null) {
                            scoreInput?.rating = data.score.toFloat()
                        }
                        titleInput?.setText(data.title)
                        reviewInput?.setText(data.content)
                        favLyricInput?.setText(data.favorite_lyrics)
                        worstLyricInput?.setText(data.worst_lyrics)

                        val favIndex = mutableSongsList.indexOf(data.favorite_song)
                        if(favIndex > -1) {
                            spinner?.setSelection(favIndex)
                        }
                        val worstIndex = mutableSongsList.indexOf(data.worst_song)
                        if(worstIndex > -1) {
                            spinner2?.setSelection(worstIndex)
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error decoding existingReview", e) // Log the error
                    // Optionally show a Toast or handle the error gracefully
                }
            }


            postButton?.setOnClickListener {
                val title = reviewInput?.text.toString() // Note: This is using reviewInput text for title
                val reviewContent = reviewInput?.text.toString() // Renamed to avoid conflict
                val favLyric = favLyricInput?.text.toString()
                val worstLyric = worstLyricInput?.text.toString()

                if(reviewContent.isBlank()) { // Check reviewContent
                    return@setOnClickListener
                }

                lifecycleScope.launch {
                    postButton?.text = ""
                    progressBar?.visibility = View.VISIBLE
                    postButton.isEnabled = false

                    val res: Result<*> // Use Result type as suggested by the usage of .isSuccess

                    Log.d("Existing Review", existingReview.toString())
                    if(existingReview != null && existingReview!!.isNotBlank()) { // Added null and blank check
                        val prevData = jsonParser.decodeFromString(Review.serializer(), existingReview!!)
                        res = update_review(
                            prevData.id.toString(),
                            userId.toString(),
                            albumInfo.id ?: "",
                            title, // Using the title from reviewInput
                            reviewContent, // Using the review content
                            java.time.LocalDateTime.now(),
                            score,
                            favLyric,
                            favSong,
                            worstLyric,
                            worstSong
                        )
                    } else {
                        res = insert_review(
                            userId.toString(),
                            albumInfo.id ?: "",
                            title, // Using the title from reviewInput
                            reviewContent, // Using the review content
                            java.time.LocalDateTime.now(),
                            score,
                            favLyric,
                            favSong,
                            worstLyric,
                            worstSong
                        )
                    }
                    if(res.isSuccess) {
                        progressBar?.visibility = View.GONE // Changed to GONE on success
                        postButton?.text = "Post"
                        postButton.isEnabled = true
                        dismiss()
                    } else {
                        // Handle error case if insert_review fails
                        progressBar?.visibility = View.GONE
                        postButton?.text = "Post"
                        postButton.isEnabled = true
                        Toast.makeText(requireContext(), "Failed to post review", Toast.LENGTH_SHORT).show()
                    }
                }
            }


            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.apply {
                    isHideable = true
//                    skipCollapsed = false
//                    isFitToContents = true
                    isDraggable = true
//                    halfExpandedRatio = 0.5f
                    expandedOffset = 0
                    state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
        }

        return dialog
    }

    companion object {
        const val TAG = "DialogReviewBottomSheet"
        private const val ARG_ALBUM_INFO = "album_info_json" // <-- Unique key
        private const val ARG_EXISTING_REVIEW = "existing_review_json" // <-- Unique key

        // Factory method to create a new instance with arguments
        fun newInstance(albumJson: String): DialogReviewBottomSheet {
            val fragment = DialogReviewBottomSheet()
            val args = Bundle().apply {
                putString(ARG_ALBUM_INFO, albumJson)
            }
            fragment.arguments = args
            return fragment
        }

        fun newExistingInstance(albumJson: String, reviewJson: String): DialogReviewBottomSheet {
            val fragment = DialogReviewBottomSheet()
            val args = Bundle().apply {
                putString(ARG_ALBUM_INFO, albumJson) // Use unique key
                putString(ARG_EXISTING_REVIEW, reviewJson) // Use unique key
            }
            fragment.arguments = args
            return fragment
        }
    }
}
