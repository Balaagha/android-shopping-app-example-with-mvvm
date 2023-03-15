package com.example.data.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


fun <T>String.fromJsonString(): T{
    return Gson().fromJson(this, object : TypeToken<T>() {}.type)
}