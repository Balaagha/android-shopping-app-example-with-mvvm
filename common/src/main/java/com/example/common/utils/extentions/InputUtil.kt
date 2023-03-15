package com.example.common.utils.extentions

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun showSoftKey(inputTarget: EditText) {
    inputTarget.post {
        inputTarget.requestFocus()
        getInputMethodManager(inputTarget.context).showSoftInput(inputTarget, 0)
    }
}

fun showSoftKeyWithDelayed(inputTarget: EditText) {
    inputTarget.postDelayed({
        inputTarget.requestFocus()
        getInputMethodManager(inputTarget.context).showSoftInput(inputTarget, 0)
    }, 100)
}

fun getInputMethodManager(context: Context): InputMethodManager {
    return context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
}
