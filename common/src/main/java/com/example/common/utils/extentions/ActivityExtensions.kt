package com.example.common.utils.extentions

import android.app.Activity
import android.content.Intent

fun <A : Activity> Activity.openActivityAndClearStack(activity: Class<A>) {
  Intent(this, activity).apply {
    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(this)
    finish()
  }
}

fun <A : Activity> Activity.openActivity(activity: Class<A>) {
  Intent(this, activity).apply {
    startActivity(this)
  }
}