package com.example.uitoolkit.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.common.utils.extentions.asDp

class ItemDecoration(
    private val topSpace: Int,
    private val bottomSpace: Int,
    private val rightSpace: Int,
    private val leftSpace: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            top = if (parent.getChildAdapterPosition(view) == 0) {
                0.asDp
            } else {
                topSpace.asDp
            }
            left = leftSpace.asDp
            right = rightSpace.asDp
            bottom = bottomSpace.asDp
        }
    }
}





