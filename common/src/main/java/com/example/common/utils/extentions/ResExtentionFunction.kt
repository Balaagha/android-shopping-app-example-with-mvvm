package com.example.common.utils.extentions

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.common.utils.extentions.isNotNull

fun Int.asColorInt(context: Context) = ContextCompat.getColor(context, this)

fun Int?.isReferenceNotNull(): Boolean{
    return this.isNotNull() && this != 0
}