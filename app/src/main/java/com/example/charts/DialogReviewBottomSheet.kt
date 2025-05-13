package com.example.charts

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class DialogReviewBottomSheet : BottomSheetDialogFragment() {

    private var userId = MyUserObject.myUserId
    private var albumJSON: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Retrieve the arguments from the Bundle
        arguments?.let {
            albumJSON = it.getString(ARG_ALBUM_INFO) ?: ""
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
            var score: Float = 0.0f
            val scoreInput: RatingBar? = bottomSheetDialog.findViewById(R.id.scoreRatingBar)
            val progressBar: ProgressBar? = bottomSheetDialog.findViewById(R.id.buttonProgressBar)

            scoreInput?.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                score = rating
            }

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
                        "",
                        worstLyric,
                        ""
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

        // Factory method to create a new instance with arguments
        fun newInstance(albumJson: String): DialogReviewBottomSheet {
            val fragment = DialogReviewBottomSheet()
            val args = Bundle().apply {
                putString(ARG_ALBUM_INFO, albumJson)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
