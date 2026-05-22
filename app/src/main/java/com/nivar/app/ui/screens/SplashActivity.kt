package com.nivar.app.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.nivar.app.MainActivity
import com.nivar.app.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * [SplashActivity] - The first screen users see when launching Nivar
 * 
 * Purpose:
 * - Display brand identity (logo + tagline)
 * - Provide smooth app initialization experience
 * - Optional: Pre-load critical resources
 * 
 * Design:
 * - Solid Midnight Navy background
 * - Centered Nivar logo (Guardian Signal)
 * - Tagline: "GOVERNANCE & PROTECTION"
 * 
 * Duration: 2 seconds (configurable)
 * 
 * Implementation Notes:
 * - Uses windowBackground theme attribute for instant display
 * - Avoids layout inflation for faster perceived startup
 * - Automatically transitions to MainActivity
 */
class SplashActivity : ComponentActivity() {

    companion object {
        private const val SPLASH_DISPLAY_DURATION = 2000L // 2 seconds
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Setup the view (adds text/tagline to the splash)
        // windowBackground handles the initial "instant" display of just the logo
        setContentView(R.layout.activity_splash)
        
        // Launch coroutine to handle delayed transition
        lifecycleScope.launch {
            delay(SPLASH_DISPLAY_DURATION)
            navigateToMainActivity()
        }
    }

    /**
     * Navigate to main app screen
     * Use FLAG_ACTIVITY_CLEAR_TASK to prevent back navigation to splash
     */
    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // Close splash activity
    }
}
