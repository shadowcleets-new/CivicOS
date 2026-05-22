package com.nivar.app.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.WindowManager
import java.io.File

/**
 * [CLASS] SecurityUtils
 * [PURPOSE] Provides security checks and enforcements like Root Detection and Screen Privacy.
 */
object SecurityUtils {

    /**
     * [METHOD] Check if device is rooted.
     * [PURPOSE] Rooted devices compromise app security sandbox.
     */
    fun isDeviceRooted(): Boolean {
        // [CHECK 1] Test Keys
        val buildTags = Build.TAGS
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true
        }

        // [CHECK 2] Common SU binary paths
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/su/bin/su",
            "/system/xbin/magisk",
            "/data/adb/magisk"
        )

        for (path in paths) {
            if (File(path).exists()) {
                return true
            }
        }

        // [CHECK 3] Execute SU command (Most reliable)
        var process: Process? = null
        return try {
            process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
            val reader = process.inputStream.bufferedReader()
            reader.readLine() != null
        } catch (t: Throwable) {
            false
        } finally {
            process?.destroy()
        }
    }

    /**
     * [METHOD] Check if running on an emulator.
     * [PURPOSE] Detect if environment is simulated (often used for analysis).
     */
    fun isEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk" == Build.PRODUCT)
    }

    /**
     * [METHOD] Enable Screen Privacy (Prevent Screenshots/Recording)
     * [PURPOSE] Prevent malware from scraping screen data or users from taking unauthorized screenshots.
     * [USAGE] Call in Activity.onCreate() or Composable DisposableEffect.
     */
    fun setScreenPrivacy(activity: Activity, enable: Boolean) {
        if (enable) {
            activity.window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        } else {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }
}
