plugins {
    id(Plugins.library)
    id(Plugins.kotlinAndroid)
    id(Plugins.kotlinAndroidExtensions)
    id(Plugins.kotlinKapt)
    id(Plugins.daggerHiltAndroidPlugin)
    id(Plugins.kotlinxSerialization)
}

android {
    compileSdk = ConfigData.compileSdk

    defaultConfig {
        minSdk = ConfigData.minSdk
        targetSdk = ConfigData.targetSdk
    }

    buildTypes {
        release { // alternative is -> getByName("release") {
            isMinifyEnabled = ConfigData.debugMinifyEnabled
            proguardFiles(getDefaultProguardFile(ConfigData.defaultProguardFile), ConfigData.proguardRules)
        }
    }

    flavorDimensions += "default"
    productFlavors{
        create("prod"){
            dimension = "default"
        }
        create("dev"){
            dimension = "default"
        }
        create("mock"){
            dimension = "default"
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

    configurations.all {
        exclude("org.jetbrains.kotlin", "kotlin-parcelize-runtime")
    }
}

dependencies {
    // App Libraries
    implementation(AppDependencies.commonImplementationLibraries)
    implementation(AppDependencies.kotlinX.kotlinxSerializationImpl)
    kapt(AppDependencies.commonKaptLibraries)

    implementation(project(Modules.data))
    implementation(project(Modules.common))
    implementation(project(Modules.uiToolKit))

    // Test Libraries
    testImplementation(AppDependencies.testLibraries)
    androidTestImplementation(AppDependencies.androidTestLibraries)
}