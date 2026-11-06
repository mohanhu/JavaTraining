import org.gradle.kotlin.dsl.debugImplementation

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.codelooper.training"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.codelooper.training"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunner = "com.codelooper.training.core.HiltAndroidRunner"
    }

    buildTypes {
        release {
//            isShrinkResources  = true
//            isMinifyEnabled =  true
//            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        viewBinding = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.fragment)
    implementation(libs.material)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Unit Testing Dependencies (src/test)
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.core.testing)
    testImplementation(libs.mockwebserver)

    // Android Instrumented / UI Testing Dependencies (src/androidTest)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.espresso.contrib)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.mockito.android)
    androidTestImplementation(libs.core.testing)
    androidTestImplementation(libs.mockwebserver)
    androidTestImplementation(libs.fragment.testing)

    // Hilt Android testing support
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.56")
    androidTestAnnotationProcessor("com.google.dagger:hilt-android-compiler:2.56")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")


    //Hilt
    implementation(libs.hilt.android)
    annotationProcessor(libs.hilt.compiler)

    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.11.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.11.0")

    // Core Glide library
    implementation(libs.glide)

    // Annotation processor for Kotlin (requires KSP plugin)
    annotationProcessor(libs.glide.ksp)

    // OR Annotation processor for Java
    annotationProcessor(libs.glide.compiler)

    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation("androidx.room:room-runtime:2.8.4")
    annotationProcessor("androidx.room:room-compiler:2.8.4")

    debugImplementation ("com.squareup.leakcanary:leakcanary-android:2.14")
}