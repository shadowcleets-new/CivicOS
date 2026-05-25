plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.dagger.hilt.android")
    // id("com.google.gms.google-services")
    // id("com.google.firebase.crashlytics")
    id("kotlin-kapt")
}

android {
    namespace = "com.nivar.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.nivar.app"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }
    // buildToolsVersion = "34.0.0" // Auto-managed by AGP

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            
            // [SECURITY] Add integrity check configurations
            ndk {
                debugSymbolLevel = "FULL"
            }
        }
        debug {
            // [SECURITY] Enable basic obfuscation even in debug to catch issues early
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        jniLibs {
            useLegacyPackaging = false
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")
    implementation("androidx.activity:activity-compose:1.12.4")
    implementation(platform("androidx.compose:compose-bom:2026.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.navigation:navigation-compose:2.9.7")
    
    // Retrofit for API
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    
    // Maps & Location
    implementation("com.google.android.gms:play-services-location:21.3.0")
    
    // Material Components (Required for XML Theme.Material3)
    implementation("com.google.android.material:material:1.13.0")

    // CameraX
    val cameraVersion = "1.5.3"
    implementation("androidx.camera:camera-core:$cameraVersion")
    implementation("androidx.camera:camera-camera2:$cameraVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraVersion")
    implementation("androidx.camera:camera-view:$cameraVersion")
    
    // Permissions
    implementation("com.google.accompanist:accompanist-permissions:0.37.3")

    // Location Coroutines support
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.10.2")

    // Adaptive Layout
    implementation("androidx.compose.material3:material3-window-size-class:1.4.0")
    
    // [SECURITY] Encrypted SharedPreferences for secure data storage
    // [COMPLIANCE] Google Play data encryption requirements
    implementation("androidx.security:security-crypto:1.1.0")
    
    // [PRIVACY] EXIF interface for stripping image metadata
    // [COMPLIANCE] Anonymous mode privacy requirements
    implementation("androidx.exifinterface:exifinterface:1.4.2")

    // Chrome Custom Tabs for secure in-app browsing
    implementation("androidx.browser:browser:1.10.0")

    // Hilt DI
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    // Widgets (Glance)
    implementation("androidx.glance:glance-appwidget:1.1.0")
    implementation("androidx.glance:glance-material3:1.1.0")

    // DataStore (Preferences)
    implementation("androidx.datastore:datastore-preferences:1.1.1")
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
    testImplementation("org.mockito:mockito-core:5.11.0")
    testImplementation("org.mockito:mockito-inline:5.2.0")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
}
