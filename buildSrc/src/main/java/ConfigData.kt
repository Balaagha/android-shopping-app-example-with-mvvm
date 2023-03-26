object ConfigData {
    const val compileSdk = 33
    const val applicationId = "com.example.androidmvvmcleanarchitectureexample"
    const val minSdk = 21
    const val targetSdk = 33
    const val versionCode = 5
    const val versionName = "5.0"
    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    const val jvmTarget = "1.8"

    const val releaseMinifyEnabled = true
    const val debugMinifyEnabled = false
    const val defaultProguardFile = "proguard-android-optimize.txt"
    const val proguardRules = "proguard-rules.pro"

    const val dataBinding = true
    const val viewBinding = true

}

object Dependencies {
    const val jitPackURL = "https://jitpack.io"
    const val gradle = "com.android.tools.build:gradle:${Versions.gradleVersion}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinVersion}"
    const val navigationSafeArgsPlugin = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navVersion}"
    const val hiltAndroidGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.daggerHiltVersion}"
    const val kotlinSerializationClassPath = "org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlinVersion}"
    const val googleServices = "com.google.gms:google-services:4.3.15"
}

object Modules {
    const val data = ":data"
    const val common = ":common"
    const val core = ":core"
    const val domain = ":domain"
    const val presentation = ":presentation"
    const val uiToolKit = ":uitoolkit"
}