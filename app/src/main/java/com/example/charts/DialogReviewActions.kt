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

class DialogReviewActions : BottomSheetDialogFragment() {

    private var reviewId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Retrieve the arguments from the Bundle
        arguments?.let {
            reviewId = it.getString(ARG_REVIEW_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.bottom_sheet_review_actions, container, false)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog

            // Get the internal FrameLayout of the bottom sheet dialog
            val bottomSheet = bottomSheetDialog.findViewById<FrameLayout>(android.R.id.content)?.getChildAt(0)


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
        const val TAG = "DialogReviewActions"
        private const val ARG_REVIEW_ID = "review_id"

        // Factory method to create a new instance with arguments
        fun newInstance(reviewId: String): DialogReviewActions {
            val fragment = DialogReviewActions()
            val args = Bundle().apply {
                putString(ARG_REVIEW_ID, reviewId)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
