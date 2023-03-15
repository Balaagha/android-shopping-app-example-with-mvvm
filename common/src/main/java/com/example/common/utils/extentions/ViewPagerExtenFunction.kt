package com.example.common.utils.extentions

import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

fun ViewPager2.applyScaleTransformer() {
    setPageTransformer(CompositePageTransformer().apply {
        addTransformer { page, position ->
            val scale = 1 - (abs(position) / 3)
            page.scaleX = scale
            page.scaleY = scale
        }
    })
}