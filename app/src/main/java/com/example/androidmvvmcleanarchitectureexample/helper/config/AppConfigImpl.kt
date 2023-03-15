package com.example.androidmvvmcleanarchitectureexample.helper.config

import android.content.Context
import android.os.Build
import com.example.androidmvvmcleanarchitectureexample.BuildConfig
import com.example.data.helper.config.AppConfig
import com.example.data.helper.models.typedefs.BuildVersions
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppConfigImpl @Inject constructor(
    @ApplicationContext val context: Context,
) : AppConfig {

    override val isProduction: Boolean
        get() = BuildConfig.FLAVOR == BuildVersions.PROD

    override val isDev: Boolean
        get() = BuildConfig.FLAVOR == BuildVersions.DEV

    override val isMock: Boolean
        get() = BuildConfig.FLAVOR == BuildVersions.MOCK

    override val osVersion: String
        get() = Build.VERSION.RELEASE

    override val appBuildNumber: String
        get() = BuildConfig.VERSION_CODE.toString()

    override val appVersion: String
        get() = BuildConfig.VERSION_NAME

    override var appLanguage: String = "AZ"

}