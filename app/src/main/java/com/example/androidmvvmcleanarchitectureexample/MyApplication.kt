package com.example.androidmvvmcleanarchitectureexample

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.data.base.commonimpl.NetworkStatusListenerHelper
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application() {
    @Inject
    lateinit var networkStatusListenerHelper: NetworkStatusListenerHelper

    override fun onCreate() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate()
        networkStatusListenerHelper.init()
    }

}