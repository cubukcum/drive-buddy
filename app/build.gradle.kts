plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.googleAndroidLibrariesMapsplatformSecretsGradlePlugin)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.drive_buddy"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.example.drive_buddy"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        externalNativeBuild {
            cmake {
                (file("../opencvsdk490").absolutePath + "/sdk/native/jni")
                arguments(
                    "-DOpenCV_DIR=" + file("../opencvsdk490").absolutePath + "/sdk/native/jni",
                    "-DANDROID_TOOLCHAIN=clang",
                    "-DANDROID_STL=c++_shared"
                )
                cppFlags("")
            }
        }
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
    externalNativeBuild {
        cmake {
            path(file("src/main/cpp/CMakeLists.txt"))
            version = "3.18.1"
        }
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(libs.androidx.ui.android)
    implementation(libs.androidx.animation.core.android)
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.material3.android)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.navigation.compose)
    val camerax_version = "1.3.0"
    implementation("androidx.camera:camera-core:${camerax_version}")
    implementation("androidx.camera:camera-camera2:${camerax_version}")
    implementation("androidx.camera:camera-lifecycle:${camerax_version}")
    implementation("androidx.camera:camera-view:${camerax_version}")

    implementation("org.tensorflow:tensorflow-lite-metadata:0.1.0")
    implementation("org.tensorflow:tensorflow-lite-gpu:2.3.0")
    implementation("org.tensorflow:tensorflow-lite:2.14.0")
    implementation("org.tensorflow:tensorflow-lite-support:0.4.4")

    implementation("androidx.media3:media3-exoplayer:1.3.1")
    implementation("androidx.media3:media3-exoplayer-dash:1.3.1")
    implementation("androidx.media3:media3-ui:1.3.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // güncelleme çalışmaları
    implementation("androidx.compose.runtime:runtime:1.6.7")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.7")
    implementation("androidx.compose.runtime:runtime-rxjava2:1.6.7")

    implementation ("androidx.navigation:navigation-fragment-ktx:2.4.2")
    implementation ("androidx.constraintlayout:constraintlayout-compose:1.1.0-alpha02")
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.24.10-beta")
    implementation ("androidx.activity:activity-compose:1.4.0")

    implementation ("androidx.compose.ui:ui:1.6.7")
    implementation ("androidx.compose.material:material:1.6.7")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.6.7")
    val nav_version = "2.7.7"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    implementation("androidx.navigation:navigation-compose:$nav_version")
    val lifecycle_version = "2.7.0"
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycle_version")


    //google map işlemleri
    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.maps.android:maps-utils-ktx:3.0.0")
    implementation ("com.google.maps.android:maps-ktx:3.0.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    
    implementation(project(":opencvsdk490"))
    implementation("com.jakewharton.timber:timber:5.0.1")


    implementation("org.tensorflow:tensorflow-lite-task-vision:+")
    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.2")


}