package com.example.uitoolkit.utils

import android.annotation.SuppressLint
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

const val UTC = "UTC"

// format examples => "dd.MM.yyyy","dd MMM yyyy"
fun getStringInMillis(date: String, format: String): Long {
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    return try {
        sdf.parse(date).time
    } catch (ex: Exception) {
        MaterialDatePicker.todayInUtcMilliseconds()
    }
}

// format examples => "dd.MM.yyyy","dd MMM yyyy"
@SuppressLint("SimpleDateFormat")
fun getDateStringFromLong(milly: Long, format: String): String {
    val date = Date(milly)
    val sdf = SimpleDateFormat(format)
    return sdf.format(date)
}

fun getYearRoll(period: Int): Long {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone(UTC))
    calendar.add(Calendar.YEAR, period)
    return calendar.timeInMillis
}
