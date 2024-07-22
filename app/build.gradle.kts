plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.alawnehj.mytvapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.alawnehj.mytvapplication"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures{
        dataBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.google.material)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.picasso)
    implementation(libs.lifecycle.extensions)
    implementation(libs.room.runtime)
    implementation(libs.room.rxjava2)
    implementation(libs.rxandroid)
    implementation(libs.sdp.android)
    implementation(libs.ssp.android)
    implementation(libs.roundedimageview)
    annotationProcessor(libs.room.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}