package com.example.charts

import android.app.Dialog
import android.os.Bundle
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
        // Retrieve the arguments from the Bundle
        arguments?.let {
            albumJSON = it.getString(ARG_ALBUM_INFO) ?: ""
            existingReview = it.getString(ARG_EXISTING_REVIEW)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.bottom_sheet_review, container, false)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val albumInfo =  albumJSON.let {
                Json.decodeFromString(AlbumInfo.serializer(), it)
        }

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
            spinner2?.adapter = spinnerAdapter
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
//
//            if(existingReview !== "" || existingReview !== null) {
//                val prevData = existingReview?.let { Json.decodeFromString(Review.serializer(), it) }
//                if(prevData == null) return@setOnShowListener
//                if(prevData.score == null) return@setOnShowListener
//
//                scoreInput?.rating = prevData.score.toFloat()
//                titleInput?.setText(prevData.title)
//                reviewInput?.setText(prevData.content)
//                favLyricInput?.setText(prevData.favorite_lyrics)
//                worstLyricInput?.setText(prevData.worst_lyrics)
//
//                val favIndex = mutableSongsList.indexOf(prevData.favorite_song)
//                if(favIndex > -1) {
//                    spinner?.setSelection(favIndex)
//                }
//                val worstIndex = mutableSongsList.indexOf(prevData.worst_song)
//                if(worstIndex > -1) {
//                    spinner2?.setSelection(worstIndex)
//                }
//            }

            postButton?.setOnClickListener {
                val title = reviewInput?.text.toString()
                val review = reviewInput?.text.toString()
                val favLyric = favLyricInput?.text.toString()
                val worstLyric = worstLyricInput?.text.toString()

                if(review.isBlank()) {
                    return@setOnClickListener
                }

                lifecycleScope.launch {
                    postButton?.text = ""
                    progressBar?.visibility = View.VISIBLE
                    postButton.isEnabled = false
                    var res = insert_review(
                        userId.toString(),
                        albumInfo.id ?: "",
                        title,
                        review,
                        java.time.LocalDateTime.now(),
                        score,
                        favLyric,
                        favSong,
                        worstLyric,
                        worstSong
                    )
                    if(res.isSuccess) {
                        progressBar?.visibility = View.VISIBLE
                        postButton?.text = "Post"
                        postButton.isEnabled = true
                        dismiss()
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
        private const val ARG_ALBUM_INFO = ""
        private const val ARG_EXISTING_REVIEW = ""

        // Factory method to create a new instance with arguments
        fun newInstance(albumJson: String): DialogReviewBottomSheet {
            val fragment = DialogReviewBottomSheet()
            val args = Bundle().apply {
                putString(ARG_ALBUM_INFO, albumJson)
            }
            fragment.arguments = args
            return fragment
        }

        fun newExistingInstance(albumJson: String, review: String): DialogReviewBottomSheet {
            val fragment = DialogReviewBottomSheet()
            val args = Bundle().apply {
                putString(ARG_ALBUM_INFO, albumJson)
                putString(ARG_EXISTING_REVIEW, review)
            }
            fragment.arguments = args
            return fragment
        }    }
}
