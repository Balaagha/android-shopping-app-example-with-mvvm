package com.example.data.helper.config


interface AppConfig {
    val isProduction: Boolean
    val isDev: Boolean
    val isMock: Boolean
    val osVersion: String
    val appBuildNumber: String
    val appVersion: String
    var appLanguage: String
}