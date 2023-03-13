import org.gradle.api.artifacts.dsl.DependencyHandler

object AppDependencies {
    val commonImplementationLibraries = arrayListOf<String>().apply {
        add(AppCompat.appcompat)

        add(View.constraintLayout)
        add(View.materialDesign)

        add(Ktx.coreKtx)
        add(Ktx.activityKtx)
        add(Ktx.fragmentKtx)

        add(NavigationComponent.navigationFragmentKtx)
        add(NavigationComponent.navigationUiKtx)

        add(Dagger.daggerHiltAndroid)
        add(Dagger.hiltNavigation)
        add(Dagger.hiltNavigationFragment)
        add(Dagger.hiltLifecycleViewModel)

        add(Lifecycle.lifecycleLivedataKtx)
        add(Lifecycle.lifecycleViewModelKtx)
        add(Lifecycle.lifecycleRuntimeKtx)
        add(Lifecycle.lifecycleExtensions)
        add(Lifecycle.lifecycleViewModelSavedState)

        add(Room.roomKtx)
        add(Room.roomRuntime)

        add(DataStore.datastorePreferences)

        add(Coroutines.kotlinxCoroutinesCore)
        add(Coroutines.kotlinxCoroutinesAndroid)

        add(Coil.coil)
        add(Coil.coilBase)
        add(Coil.coilGif)

        add(Network.retrofit2)
        add(Network.googleCodeGson)
        add(Network.retrofit2ConverterGson)
        add(Network.okhttp3LoggingInterceptor)

        add(Shimmer.shimmer)
        add(Shimmer.shimmerRecyclerView)

        add(WorkManager.workRuntime)
        add(WorkManager.workRuntimeKtx)
        add(WorkManager.workGcm)

        add(RxJava.rx_binding3Core)

        add(Jsoup.jsoup)

        add(Animation.lottie)

        add(Glide.glideImpl)
        add(GoogleAndroidGms.authApiPhone)

    }

    val commonKaptLibraries = arrayListOf<String>().apply {
        add(Dagger.hiltCompilerKaptProcessor)
        add(Dagger.hiltAndroidCompilerKaptProcessor)

        add(DataBinding.dataBindingCompilerKaptProcessor)
        add(DataBinding.dataBindingCommonKaptProcessor)

        add(Room.roomCompilerKaptProcessor)

    }

    val commonAnnotationProcessorLibraries = arrayListOf<String>().apply {
        add(Glide.glideCompiler)
    }

    val testLibraries = arrayListOf<String>().apply {
        add(Dagger.daggerHiltAndroidTesting)

        add(TestImplementationLibraries.coreTesting)
        add(TestImplementationLibraries.retrofit2)
        add(TestImplementationLibraries.junit)
        add(TestImplementationLibraries.kotlinxCoroutinesTest)
    }

    val androidTestLibraries = arrayListOf<String>().apply {
        add(AndroidTestImplementationLibraries.espressoCore)
        add(AndroidTestImplementationLibraries.junit)
        add(AndroidTestImplementationLibraries.roomTesting)
    }

    object Animation {
        const val lottie = "com.airbnb.android:lottie:2.6.0"
    }

    object AppCompat {
        const val appcompat = "androidx.appcompat:appcompat:${Versions.appCompatVersion}"
    }

    object kotlinX {
        const val kotlinxSerializationImpl = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2"
    }

    object View {
        const val materialDesign = "com.google.android.material:material:${Versions.materialVersion}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayoutVersion}"
    }

    object Ktx {
        const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtxVersion}"
        const val activityKtx = "androidx.activity:activity-ktx:${Versions.activityKtxVersion}"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragmentKtxVersion}" // relating with view model
    }

    object Lifecycle {
        const val lifecycleViewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleVersion}"
        const val lifecycleLivedataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleVersion}"
        const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleVersion}"

        const val lifecycleViewModelSavedState = "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.lifecycleVersion}"

        const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycleExtensions}"
    }

    object NavigationComponent {
        const val navigationFragmentKtx = "androidx.navigation:navigation-fragment-ktx:${Versions.navVersion}"
        const val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:${Versions.navVersion}"
    }

    object Dagger {
        const val daggerHiltAndroid = "com.google.dagger:hilt-android:${Versions.daggerHiltVersion}"
        const val daggerHiltAndroidTesting = "com.google.dagger:hilt-android-testing:${Versions.daggerHiltVersion}"

        const val hiltAndroidCompilerKaptProcessor = "com.google.dagger:hilt-android-compiler:${Versions.daggerHiltVersion}"

        const val hiltNavigationFragment = "androidx.hilt:hilt-navigation-fragment:${Versions.hiltNavigationFragmentVersion}"
        const val hiltNavigation = "androidx.hilt:hilt-navigation:${Versions.hiltNavigationVersion}"
        const val hiltLifecycleViewModel = "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.hiltLifecycleViewModelVersion}"

        const val hiltCompilerKaptProcessor = "androidx.hilt:hilt-compiler:${Versions.hiltCompilerVersion}"
    }

    object DataBinding {
        const val dataBindingCompilerKaptProcessor = "com.android.databinding:compiler:${Versions.dataBindingCompilerVersion}"
        const val dataBindingCommonKaptProcessor = "androidx.databinding:databinding-common:${Versions.dataBindingCommonVersion}"
    }

    object DataStore {
        const val datastorePreferences = "androidx.datastore:datastore-preferences:${Versions.datastorePreferencesVersion}"
    }

    object Room {
        const val roomRuntime = "androidx.room:room-runtime:${Versions.roomVersion}"
        const val roomKtx = "androidx.room:room-ktx:${Versions.roomVersion}"
        const val roomCompilerKaptProcessor = "androidx.room:room-compiler:${Versions.roomVersion}"
    }

    object Coroutines {
        const val kotlinxCoroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutineVersion}"
        const val kotlinxCoroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutineVersion}"
    }

    object Coil {
        const val coil = "io.coil-kt:coil:${Versions.coilVersion}"
        const val coilBase = "io.coil-kt:coil-base:${Versions.coilVersion}"
        const val coilGif = "io.coil-kt:coil-gif:${Versions.coilVersion}"

    }

    object Glide {
        const val glideImpl = "com.github.bumptech.glide:glide:${Versions.glideVersion}"
        const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glideVersion}"
    }

    object GoogleAndroidGms {
        const val authApiPhone = "com.google.android.gms:play-services-auth-api-phone:18.0.1"

    }

    object Network {
        const val retrofit2 = "com.squareup.retrofit2:retrofit:2.9.0"
        const val retrofit2ConverterGson = "com.squareup.retrofit2:converter-gson:2.9.0"
        const val googleCodeGson = "com.google.code.gson:gson:2.8.6"
        const val okhttp3LoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:4.9.0"
    }

    object Shimmer {
        const val shimmer = "com.facebook.shimmer:shimmer:0.5.0"
        const val shimmerRecyclerView = "com.todkars:shimmer-recyclerview:0.4.0"
    }

    object Jsoup {
        const val jsoup = "org.jsoup:jsoup:1.13.1"
    }

    object TestImplementationLibraries {
        const val junit = "junit:junit:4.13.2"
        const val coreTesting = "androidx.arch.core:core-testing:2.1.0"
        const val kotlinxCoroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2"
        const val retrofit2 = "com.squareup.retrofit2:retrofit-mock:2.9.0"
    }

    object AndroidTestImplementationLibraries {
        const val junit = "androidx.test.ext:junit:1.1.3"
        const val espressoCore = "androidx.test.espresso:espresso-core:3.4.0"
        const val roomTesting = "androidx.room:room-testing:2.2.5"
    }

    object WorkManager {
        val work_version = "2.7.0"

        // (Java only)
        val workRuntime = "androidx.work:work-runtime:$work_version"

        // Kotlin + coroutines
        val workRuntimeKtx =  "androidx.work:work-runtime-ktx:$work_version"

        // optional - GCMNetworkManager support
        val workGcm = "androidx.work:work-gcm:$work_version"

    }
    object RxJava {
        val rxKotlinVersion = "3.0.1"
        val rxAndroidVersion = "3.0.0"
        val rxJavaVersion = "3.1.4"
        val rxBindingVersion = "4.0.0"

        val rx_java = "io.reactivex.rxjava3:rxjava:${rxJavaVersion}"
        val rx_kotlin = "io.reactivex.rxjava3:rxkotlin:${rxKotlinVersion}"
        val rx_java_android = "io.reactivex.rxjava3:rxandroid:${rxAndroidVersion}"
        val rx_binding3Core = "com.jakewharton.rxbinding4:rxbinding-core:${rxBindingVersion}"



    }

}


fun DependencyHandler.implementation(list: List<String>) {
    list.forEach { dependency ->
        add("implementation", dependency)
    }
}

fun DependencyHandler.annotationProcessor(list: List<String>) {
    list.forEach { dependency ->
        add("annotationProcessor", dependency)
    }
}

fun DependencyHandler.kapt(list: List<String>) {
    list.forEach { dependency ->
        add("kapt", dependency)
    }
}

fun DependencyHandler.androidTestImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("androidTestImplementation", dependency)
    }
}

fun DependencyHandler.testImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("testImplementation", dependency)
    }
}