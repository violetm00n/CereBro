plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    //id ("com.android.application")
    id("com.google.gms.google-services")

}

android {
    namespace = "com.example.dyslexiaanalyzer"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.dyslexiaanalyzer"
        minSdk = 24
        targetSdk = 35
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.ml.modeldownloader.ktx)
    implementation(libs.core.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.vision.internal.vkp)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
    implementation("com.google.firebase:firebase-analytics")
    //implementation ("com.google.firebase:firebase-ml-model-interpreter:22.0.0")
    implementation ("androidx.activity:activity-compose:1.7.2")
    implementation ("androidx.compose.ui:ui:1.5.1")
    implementation ("androidx.compose.material:material:1.5.1")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.5.1")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation ("com.google.firebase:firebase-ml-modeldownloader:25.0.1")

    implementation ("com.itextpdf:itext7-core:7.2.5")
    implementation ("com.google.firebase:firebase-ml-model-interpreter:22.0.4")
    implementation ("org.tensorflow:tensorflow-lite:2.8.0")
    implementation ("org.tensorflow:tensorflow-lite-support:0.3.0")
    implementation("com.google.accompanist:accompanist-navigation-animation:0.32.0") // Smooth navigation animations
    implementation("com.airbnb.android:lottie-compose:6.2.0")
    implementation("io.coil-kt:coil-compose:2.5.0")
}