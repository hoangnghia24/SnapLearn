plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "hcmute.edu.vn.snaplearn"
    compileSdk {
        version = release(36)
    }
    defaultConfig {
        applicationId = "hcmute.edu.vn.snaplearn"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // To recognize Latin script
    implementation("com.google.mlkit:text-recognition:16.0.1")
// To recognize Chinese script
    implementation("com.google.mlkit:text-recognition-chinese:16.0.1")
// To recognize Devanagari script
    implementation("com.google.mlkit:text-recognition-devanagari:16.0.1")
// To recognize Japanese script
    implementation("com.google.mlkit:text-recognition-japanese:16.0.1")
// To recognize Korean script
    implementation("com.google.mlkit:text-recognition-korean:16.0.1")
    // To recognize Latin script
    implementation("com.google.android.gms:play-services-mlkit-text-recognition:19.0.1")
// To recognize Chinese script
    implementation("com.google.android.gms:play-services-mlkit-text-recognition-chinese:16.0.1")
// To recognize Devanagari script
    implementation("com.google.android.gms:play-services-mlkit-text-recognition-devanagari:16.0.1")
// To recognize Japanese script
    implementation("com.google.android.gms:play-services-mlkit-text-recognition-japanese:16.0.1")
// To recognize Korean script
    implementation("com.google.android.gms:play-services-mlkit-text-recognition-korean:16.0.1")
    implementation("com.google.mlkit:language-id:17.0.6")
    implementation("com.google.android.gms:play-services-mlkit-language-id:17.0.0")
    implementation("com.google.mlkit:translate:17.0.3")
//add firebase
    implementation(platform("com.google.firebase:firebase-bom:34.12.0"))
    implementation("com.google.firebase:firebase-analytics")
    // Add the dependency for the Firebsase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth")

    // Also add the dependency for the Google Play services library and specify its version
    implementation("com.google.android.gms:play-services-auth:21.5.1")
    // Declare the dependency for the Cloud Firestore library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-firestore")
    // --- CameraX (Dùng để mở Camera quét chữ) ---
    val camerax_version = "1.3.0"
    implementation("androidx.camera:camera-core:$camerax_version")
    implementation("androidx.camera:camera-camera2:$camerax_version")
    implementation("androidx.camera:camera-lifecycle:$camerax_version")
    implementation("androidx.camera:camera-view:$camerax_version")

    // --- Room Database (Dùng để lưu Flashcard offline) ---
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    // Thư viện hỗ trợ cắt ảnh cực xịn (Canhub Image Cropper)
    implementation("com.vanniktech:android-image-cropper:4.6.0")
}