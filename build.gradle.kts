// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(Dependencies.gradle)
        classpath(Dependencies.kotlinGradlePlugin)
        classpath(Dependencies.navigationSafeArgsPlugin)
        classpath(Dependencies.hiltAndroidGradlePlugin)
        classpath(Dependencies.kotlinSerializationClassPath)
        classpath("com.google.gms:google-services:4.3.10")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }

}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven(url = Dependencies.jitPackURL)
    }
}

subprojects {
    repositories {
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

tasks { // ./gradlew cleanMe
    val cleanMe by registering(Delete::class){
        println("start")
        delete(buildDir)
        println("end")
    }
}