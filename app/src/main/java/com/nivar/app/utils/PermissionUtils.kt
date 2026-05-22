// [MODULE] Permission Request Utilities
// [PURPOSE] Runtime permission request handling with rationale dialogs
// [COMPLIANCE] Google Play permission policy requirements

package com.nivar.app.utils

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

/**
 * [CLASS] PermissionUtils - Permission request helpers
 * [PURPOSE] Provide user-friendly permission request dialogs with rationale
 * [COMPLIANCE] Implements Google Play permission best practices
 */
object PermissionUtils {
    
    /**
     * [METHOD] Show location permission rationale dialog
     * [PURPOSE] Explain why location permission is needed before requesting
     * [PARAMS] context - Activity context for showing dialog
     * [PARAMS] onGrantClick - Callback when user clicks "Grant Permission"
     * [PARAMS] onDenyClick - Callback when user clicks "Not Now"
     * [COMPLIANCE] Google Play requires explaining permission usage
     */
    fun showLocationPermissionRationale(
        context: Context,
        onGrantClick: () -> Unit,
        onDenyClick: () -> Unit = {}
    ) {
        AlertDialog.Builder(context)
            .setTitle("Location Permission Required")  // [TITLE] Clear, concise title
            .setMessage(
                "Nivar needs your location to:\n\n" +
                "• Identify the correct government authority for your area\n" +
                "• Route your grievance to the responsible department\n" +
                "• Provide accurate jurisdiction information\n\n" +
                "Your location is only used when submitting grievances and is never shared with third parties."
            )  // [MESSAGE] Detailed explanation of permission usage
            .setPositiveButton("Grant Permission") { dialog, _ ->
                dialog.dismiss()
                onGrantClick()  // [CALLBACK] User agreed to grant permission
            }
            .setNegativeButton("Not Now") { dialog, _ ->
                dialog.dismiss()
                onDenyClick()  // [CALLBACK] User declined permission
            }
            .setCancelable(true)  // [CANCELABLE] User can dismiss with back button
            .show()
    }
    
    /**
     * [METHOD] Show camera permission rationale dialog
     * [PURPOSE] Explain why camera permission is needed
     * [PARAMS] context - Activity context
     * [PARAMS] onGrantClick - Callback when user agrees
     * [PARAMS] onDenyClick - Callback when user declines
     */
    fun showCameraPermissionRationale(
        context: Context,
        onGrantClick: () -> Unit,
        onDenyClick: () -> Unit = {}
    ) {
        AlertDialog.Builder(context)
            .setTitle("Camera Permission Required")
            .setMessage(
                "Nivar needs camera access to:\n\n" +
                "• Capture evidence photos for your grievance\n" +
                "• Document issues like potholes, garbage, broken infrastructure\n" +
                "• Strengthen your report with visual proof\n\n" +
                "Photos are stored locally until you submit your report. You can delete them anytime."
            )
            .setPositiveButton("Grant Permission") { dialog, _ ->
                dialog.dismiss()
                onGrantClick()
            }
            .setNegativeButton("Not Now") { dialog, _ ->
                dialog.dismiss()
                onDenyClick()
            }
            .setCancelable(true)
            .show()
    }
    
    /**
     * [METHOD] Show permission permanently denied dialog
     * [PURPOSE] Guide user to app settings when permission permanently denied
     * [PARAMS] context - Activity context
     * [PARAMS] permissionName - Human-readable permission name (e.g., "Location")
     * [USE CASE] When user denies permission twice (Android shows "Don't ask again")
     */
    fun showPermissionPermanentlyDeniedDialog(
        context: Context,
        permissionName: String
    ) {
        AlertDialog.Builder(context)
            .setTitle("$permissionName Permission Denied")
            .setMessage(
                "You have permanently denied $permissionName permission. " +
                "To use this feature, please enable $permissionName permission in app settings.\n\n" +
                "Settings → Apps → Nivar → Permissions → $permissionName"
            )
            .setPositiveButton("Open Settings") { dialog, _ ->
                dialog.dismiss()
                // [INTENT] Open app settings page
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(true)
            .show()
    }
    
    /**
     * [METHOD] Get required permissions for grievance submission
     * [RETURNS] Array of required permission strings
     * [USE CASE] Request multiple permissions at once
     */
    fun getGrievancePermissions(): Array<String> {
        return arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA
        )
    }
    
    /**
     * [METHOD] Get location permissions only
     * [RETURNS] Array of location permission strings
     */
    fun getLocationPermissions(): Array<String> {
        return arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}

/**
 * [USAGE EXAMPLE]
 * 
 * // In Activity or Composable:
 * val locationPermissionLauncher = rememberLauncherForActivityResult(
 *     ActivityResultContracts.RequestMultiplePermissions()
 * ) { permissions ->
 *     when {
 *         permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true -> {
 *             // Permission granted - proceed with location access
 *         }
 *         else -> {
 *             // Permission denied - show explanation or disable feature
 *         }
 *     }
 * }
 * 
 * // Show rationale before requesting
 * PermissionUtils.showLocationPermissionRationale(context,
 *     onGrantClick = {
 *         locationPermissionLauncher.launch(PermissionUtils.getLocationPermissions())
 *     }
 * )
 */
