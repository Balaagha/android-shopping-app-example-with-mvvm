package com.example.data.utils.customfunction

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException


/**
 * Get Gson data for this Type token
 */
inline fun <reified T> modelFromJsonString(json: String?): T {
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

/**
 * Get model data from given jsonFilePath
 * @param jsonFilePath
 * @param context
 */
inline fun <reified T> loadModelFromJSONAssetPath(context: Context?, jsonFilePath: String): T? {
    val jsonString: String? = try {
        context?.assets?.open(jsonFilePath)
            ?.bufferedReader()
            .use { return@use it?.readText() }
    } catch (ioException: IOException) {
        null
    }
    return Gson().fromJson(jsonString, object : TypeToken<T>() {}.type)
}