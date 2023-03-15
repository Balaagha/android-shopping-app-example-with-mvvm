package com.example.common.utils.customfunction

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import kotlin.math.roundToInt

fun dpToPx(context: Context, dp: Int): Int {
    val displayMetrics = context.resources.displayMetrics
    return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
}

fun pxToDp(px: Float, context: Context): Float {
    return (px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT))
}

fun hideSoftKeyboard(activity: Activity) {
    val inputMethodManager = activity.getSystemService(
        Activity.INPUT_METHOD_SERVICE
    ) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(
        activity.currentFocus?.windowToken, 0
    )
}

fun isNumber(s: String?): Boolean {
    return if (s.isNullOrEmpty()) false else s.all { Character.isDigit(it) }
}

/**
 * Get Gson data for this Type token
 */
fun <T> fromJson(json: String?): T {
    return Gson().fromJson(json, object : TypeToken<T>() {}.type)
}

/**
 * Get Gson string data from given jsonFilePath
 * @param jsonFilePath
 */
fun loadJSONFromAsset(context: Context?, jsonFilePath: String): String? {
    val json: String
    try {
        val openJsonFile = context?.assets?.open(jsonFilePath)
        val size =openJsonFile?.available()
        val buffer = ByteArray(size ?: 0)
        openJsonFile?.read(buffer)
        openJsonFile?.close()
        json = String(buffer)
    } catch (ex: IOException) {
        ex.printStackTrace()
        return null
    }
    return json
}