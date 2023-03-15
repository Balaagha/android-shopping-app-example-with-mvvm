@file:Suppress("DEPRECATION","unused")

package com.example.common.utils.extentions

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference


fun Context.convertDpToPixel(dp: Float): Int {
    return (dp * (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
}
fun Context.convertDpToPixel(dp: Int): Int {
    return convertDpToPixel(dp.toFloat())
}
fun Context.pixelToDp(px: Int): Int {
    return pixelToDp(px.toFloat()).toInt()
}
fun Context.pixelToDp(px: Float): Float {
    return (px / resources.displayMetrics.density)
}

val Context.screenWidth: Int
    get() = resources.displayMetrics.widthPixels

val Context.screenHeight: Int
    get() = resources.displayMetrics.heightPixels

fun Context.hasNetwork(): Boolean? {
    var isConnected: Boolean? = false // Initial Value
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
    if (activeNetwork != null && activeNetwork.isConnected)
        isConnected = true
    return isConnected
}

val Context.connectivityManager: ConnectivityManager
    get() =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

fun Context.hideKeyboard(focusedView: View? = null) {
    val view = focusedView ?: (this as? AppCompatActivity
        ?: (this as? Fragment)?.activity)?.currentFocus

    if (view != null) {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            view.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}

fun Context.setStatusBarColor(context: WeakReference<Activity>, @ColorRes colorResId: Int) {
    val window = context.get()?.window
    window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window?.statusBarColor = context.get()!!.resources.getColor(colorResId)
}

fun Context.showToast(msg: String) {
    Toast.makeText(this, msg,
        Toast.LENGTH_SHORT).show()
}
