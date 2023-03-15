package com.example.common.utils.extentions

import android.os.StrictMode
import android.view.animation.Animation
import java.math.BigDecimal


fun Animation.setOnAnimationEnd(function: () -> Unit) {
    this.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {
            //not used
        }

        override fun onAnimationEnd(animation: Animation?) {
            function.invoke()
        }

        override fun onAnimationStart(animation: Animation?) {
            //not used
        }
    })
}

fun <T> allowReads(block: () -> T): T {
    val oldPolicy = StrictMode.allowThreadDiskReads()
    try {
        return block()
    } finally {
        StrictMode.setThreadPolicy(oldPolicy)
    }
}

fun <T> allowWrites(block: () -> T): T {
    val oldPolicy = StrictMode.allowThreadDiskWrites()
    try {
        return block()
    } finally {
        StrictMode.setThreadPolicy(oldPolicy)
    }
}

fun String?.toBigDecimalSafeWay(): BigDecimal {
    return try {
        if (this == null || this == "") {
            BigDecimal("0.0")
        } else {
            this.toBigDecimal()
        }
    } catch (e: Exception) {
        BigDecimal("0.0")
    }
}

fun Double?.toBigDecimalSafeWay(): BigDecimal {
    return try {
        this?.toBigDecimal() ?: BigDecimal("0.0")
    } catch (e: Exception) {
        BigDecimal("0.0")
    }
}