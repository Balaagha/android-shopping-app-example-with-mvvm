package com.example.common.listeners

import android.text.Editable
import android.text.TextWatcher

/**
 * When we nee just to extend onTextChanged method from TextWatcher
 * we nee to disable other two methods with this listener interface
 */
interface TextChangedListener : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        //we have disabled autoimplement it yet, you can enable by overriding this method
    }

    override fun afterTextChanged(s: Editable?) {
        //we have disabled autoimplement it yet, you can enable by overriding this method
    }

}