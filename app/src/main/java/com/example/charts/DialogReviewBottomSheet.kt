package com.example.charts

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RatingBar
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

class DialogReviewBottomSheet : BottomSheetDialogFragment() {

    private var userId: String? = null
    private var albumId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Retrieve the arguments from the Bundle
        arguments?.let {
            userId = it.getString(ARG_USER_ID)
            albumId = it.getString(ARG_ALBUM_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.bottom_sheet_review, container, false)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog

            // Get the internal FrameLayout of the bottom sheet dialog
            val bottomSheet = bottomSheetDialog.findViewById<FrameLayout>(android.R.id.content)?.getChildAt(0)


            val postButton: AppCompatButton? = bottomSheetDialog.findViewById(R.id.post_button)
            val titleInput: AppCompatEditText? = bottomSheetDialog.findViewById(R.id.title_input)
            val reviewInput: AppCompatEditText? = bottomSheetDialog.findViewById(R.id.review_input)
            val favLyricInput: AppCompatEditText? = bottomSheetDialog.findViewById(R.id.favorite_lyric_input)
            val worstLyricInput: AppCompatEditText? = bottomSheetDialog.findViewById(R.id.worst_lyric_input)
            var score: Float = 0.0f
            val scoreInput: RatingBar? = bottomSheetDialog.findViewById(R.id.scoreRatingBar)

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
                    var res = insert_review(
                        userId.toString(),
                        albumId.toString(),
                        title,
                        review,
                        java.time.LocalDateTime.now(),
                        score,
                        favLyric,
                        "",
                        worstLyric,
                        ""
                    )
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
        private const val ARG_USER_ID = "user_id"
        private const val ARG_ALBUM_ID = "album_id"

        // Factory method to create a new instance with arguments
        fun newInstance(userId: String, albumId: String): DialogReviewBottomSheet {
            val fragment = DialogReviewBottomSheet()
            val args = Bundle().apply {
                putString(ARG_USER_ID, userId)
                putString(ARG_ALBUM_ID, albumId)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
