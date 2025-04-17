plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
}

android {
    namespace = "com.en.svga"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.en.svga"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(project(":lib-svga"))
    implementation(group = "androidx.viewpager2", name = "viewpager2", version = "1.0.0")
    implementation(group = "androidx.recyclerview", name = "recyclerview", version = "1.3.1")
    implementation(group = "com.github.bumptech.glide", name = "glide", version = "4.16.0")
    kapt(group = "com.github.bumptech.glide", name = "compiler", version = "4.16.0")
    kapt(group = "com.github.bumptech.glide", name = "annotations", version = "4.16.0")
}