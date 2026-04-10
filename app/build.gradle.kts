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
    implementation(platform("com.google.firebase:firebase-bom:34.12.0"))
    implementation("com.google.firebase:firebase-analytics")
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
}