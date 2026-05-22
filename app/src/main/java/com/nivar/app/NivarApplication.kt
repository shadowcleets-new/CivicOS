package com.nivar.app

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import dagger.hilt.android.HiltAndroidApp
import android.util.Log

@HiltAndroidApp
class NivarApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Firebase safely
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                // Try default init first
                try {
                    FirebaseApp.initializeApp(this)
                } catch (e: Exception) {
                    // Fallback to dummy options to prevent crash
                    val options = FirebaseOptions.Builder()
                        .setApiKey("fake_api_key")
                        .setApplicationId("com.nivar.app")
                        .setProjectId("nivar-app")
                        .build()
                    FirebaseApp.initializeApp(this, options)
                    Log.w("NivarApplication", "Firebase initialized with placeholder options. Live services will not work.")
                }
            }
        } catch (e: Exception) {
            Log.e("NivarApplication", "Critical Firebase init failure", e)
        }
    }
}
