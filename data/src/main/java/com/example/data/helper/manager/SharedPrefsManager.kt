package com.example.data.helper.manager

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import android.util.Log
import com.example.data.helper.models.typedefs.SharedTypes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPrefsManager constructor(context: Context?, @SharedTypes.SharedTypesDef sharedType: String) {

    val prefs: SharedPreferences?

    init {
        prefs = context?.getSharedPreferences(sharedType, Context.MODE_PRIVATE)
    }

    fun <T> set(key: String, value: T, isEncrypt: Boolean = false) {
        val editor = prefs?.edit()

        when (value) {
            is Int -> {
                editor?.putInt(key, value)
            }
            is Long -> {
                editor?.putLong(key, value)
            }
            is String -> {
                val data = if (isEncrypt) {
                    encrypt(value)
                } else {
                    value
                }
                Log.d("SharedPrefsManager", "set value: $value")
                Log.d("SharedPrefsManager", "set encripted data: $data")
                Log.d("SharedPrefsManager", "set decripted encripted data: ${decrypt(data)}")
                editor?.putString(key, data)
            }
            is Boolean -> {
                editor?.putBoolean(key, value)
            }
            else -> {
                editor?.putString(key, value as String)
            }
        }
        editor?.apply()
    }


    inline fun <reified T> get(key: String, default: T?,isDecrypt: Boolean= false): T? {
        return when (T::class) {
            Int::class -> {
                prefs?.getInt(key, default as Int) as T?
            }
            Long::class -> {
                prefs?.getLong(key, default as Long) as T?
            }
            String::class -> {
                var returnData = prefs?.getString(key, default as String?) as T?
                if(isDecrypt && (returnData as String?)?.isNotEmpty() == true) {
                    returnData = decrypt(returnData as String?) as T?
                }
                returnData
            }
            Boolean::class -> {
                prefs?.getBoolean(key, default as Boolean) as T?
            }
            else -> {
                prefs?.getString(key, default as String?) as T?
            }
        }
    }


    fun contains(prName: String) = try {
        prefs!!.contains(prName)
    } catch (e: Exception) {
        false
    }

    fun clearSharedPrefs() {
        val editor = prefs?.edit()
        editor?.clear()
        editor?.apply()
    }

    fun removeSharedPrefs(prName: String) {
        val editor = prefs?.edit()
        editor?.remove(prName)
        editor?.apply()
    }

    private fun encrypt(value: String): String {
        // Implement encryption algorithm here
        return Base64.encodeToString(value.toByteArray(), Base64.DEFAULT)
    }

    fun decrypt(value: String?): String? {
         return String(Base64.decode(value, Base64.DEFAULT))
    }

    /**
     * Saves object into the Preferences.
     *
     * @param `object` Object of model class (of type [T]) to save
     * @param key Key with which Shared preferences to
     **/
    fun <T> saveObject(key: String, `object`: T): Boolean {
        return try {
            val jsonString = Gson().toJson(`object`)
            prefs?.edit()?.putString(key, jsonString)?.apply()
            true
        } catch (e: Exception){
            false
        }
    }

    /**
     * This function retrieves object from saved json string
     */
    inline fun <reified T> loadObject(serializedObjectKey: String, defaultValue: T): T {
        return try {
            fromJson(prefs?.getString(serializedObjectKey, ""))
        } catch (e: Exception) {
            removeSharedPrefs(serializedObjectKey)
            return defaultValue
        }
    }

    /**
     * Get Gson data for this Typetoken
     */
    inline fun <reified T> fromJson(json: String?): T {
        return Gson().fromJson(json, object : TypeToken<T>() {}.type)
    }



}
