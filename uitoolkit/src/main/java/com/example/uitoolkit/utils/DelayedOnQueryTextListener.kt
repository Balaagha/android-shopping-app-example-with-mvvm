package com.example.uitoolkit.utils

import android.os.Handler
import androidx.appcompat.widget.SearchView

abstract class DelayedOnQueryTextListener(): SearchView.OnQueryTextListener {
    private val handler: Handler = Handler()
    private var runnable: Runnable? = null
    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(runnable != null) {
            handler.removeCallbacks(runnable!!)
        }
        runnable = Runnable { onDelayerQueryTextChange(newText) }
        handler.postDelayed(runnable!!, 400)
        return true
    }

    abstract fun onDelayerQueryTextChange(query: String?)
}