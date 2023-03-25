plugins {
    id(Plugins.application)
    id(Plugins.kotlinAndroid)
    id(Plugins.kotlinKapt)
    id(Plugins.kotlinAndroidExtensions)
    id(Plugins.navigationSafeArgsKotlin)
    id(Plugins.daggerHiltAndroidPlugin)
    id(Plugins.kotlinxSerialization)
}

android {
    compileSdk = ConfigData.compileSdk

    defaultConfig {
        applicationId = ConfigData.applicationId
        minSdk = ConfigData.minSdk
        targetSdk = ConfigData.targetSdk
        versionCode = ConfigData.versionCode
        versionName = ConfigData.versionName
        testInstrumentationRunner = ConfigData.testInstrumentationRunner
    }

    buildTypes {
        release { // alternative is -> getByName("release") {
            isMinifyEnabled = ConfigData.debugMinifyEnabled
            proguardFiles(getDefaultProguardFile(ConfigData.defaultProguardFile), ConfigData.proguardRules)
        }
    }

    flavorDimensions += "default"
    productFlavors {
        create("prod"){
            dimension = "default"
        }
        create("dev"){
            dimension = "default"
            versionNameSuffix = "-Dev"
            applicationIdSuffix = ".dev"
        }
        create("mock"){
            dimension = "default"
            versionNameSuffix = "-Mock"
            applicationIdSuffix = ".mock"
        }
    }

    buildFeatures {
        dataBinding = ConfigData.dataBinding
        viewBinding = ConfigData.viewBinding
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = ConfigData.jvmTarget
    }
}

dependencies {

    // App Libraries
    implementation(AppDependencies.commonImplementationLibraries)
    implementation(AppDependencies.kotlinX.kotlinxSerializationImpl)
    implementation(AppDependencies.Indicator.indicator)

    kapt(AppDependencies.commonKaptLibraries)
    annotationProcessor(AppDependencies.commonAnnotationProcessorLibraries)

    implementation(project(Modules.data))
    implementation(project(Modules.common))
    implementation(project(Modules.core))
    implementation(project(Modules.uiToolKit))

    // Test Libraries
    testImplementation(AppDependencies.testLibraries)
    androidTestImplementation(AppDependencies.androidTestLibraries)

}