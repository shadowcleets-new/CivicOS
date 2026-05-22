package com.nivar.app.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import android.util.Log

object LinkUtils {

    private const val TAG = "LinkUtils"

    /**
     * Opens a URL reliably using Chrome Custom Tabs or fallback to browser.
     */
    fun openUrl(context: Context, url: String) {
        try {
            val uri = Uri.parse(url)
            
            // Try to use Chrome Custom Tabs for a seamless experience
            val customTabsIntent = CustomTabsIntent.Builder()
                .setShowTitle(true)
                .build()
            
            customTabsIntent.launchUrl(context, uri)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to open URL: $url", e)
            // Ultimate fallback: Try standard ACTION_VIEW
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } catch (fallbackEx: Exception) {
                Log.e(TAG, "Fallback also failed for: $url", fallbackEx)
            }
        }
    }

    /**
     * Specific helper for social media links that might prefer their native apps
     */
    fun openSocialLink(context: Context, url: String) {
        // For X/Twitter, ensure we use x.com if not already
        var finalUrl = url
        if (url.contains("twitter.com")) {
            finalUrl = url.replace("twitter.com", "x.com")
        }
        
        // We use the standard openUrl which will handle app linking if the 
        // user has the app installed and verified for these domains.
        openUrl(context, finalUrl)
    }
}
