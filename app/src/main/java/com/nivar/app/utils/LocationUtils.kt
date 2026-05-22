// [MODULE] Location Utilities
// [PURPOSE] GPS location access with proper permission handling
// [COMPLIANCE] Google Play permission policy - no @SuppressLint bypassing

package com.nivar.app.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await

/**
 * [CLASS] LocationUtils - GPS location access with permission checks
 * [PURPOSE] Safely retrieve user location with proper permission validation
 * [COMPLIANCE] Implements runtime permission checks as required by Google Play
 * [SECURITY] No @SuppressLint - all permissions properly validated
 */
class LocationUtils(private val context: Context) {
    
    // [LOCATION CLIENT] Google Play Services Fused Location Provider
    // [PURPOSE] Provides battery-efficient location access
    // [API] Uses latest Google Play Services location APIs
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    
    /**
     * [METHOD] Get current GPS location
     * [RETURNS] Location object with lat/long or null if permission denied/error
     * [PERMISSION] Checks for ACCESS_FINE_LOCATION permission before access
     * [ACCURACY] High accuracy mode - uses GPS, WiFi, and cell towers
     * [COMPLIANCE] Proper permission check - no @SuppressLint bypass
     */
    suspend fun getCurrentLocation(): Location? {
        // [PERMISSION CHECK] Verify FINE_LOCATION permission granted
        // [COMPLIANCE] Required by Google Play - cannot bypass with @SuppressLint
        if (!hasLocationPermission()) {
            // [NO PERMISSION] Return null if permission not granted
            // [UI] Calling code should request permission from user
            return null
        }
        
        // [CANCELLATION] Token for canceling location request if needed
        val cancellationTokenSource = CancellationTokenSource()
        
        return try {
            // [LOCATION REQUEST] Request current location with high accuracy
            // [PRIORITY] PRIORITY_HIGH_ACCURACY uses GPS for precise location
            // [AWAIT] Suspend function - waits for location result
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,  // [ACCURACY] GPS + WiFi + Cell
                cancellationTokenSource.token  // [CANCELLATION] Cancel token
            ).await()  // [COROUTINE] Suspend until location received
            
        } catch (e: SecurityException) {
            // [SECURITY ERROR] Permission was revoked during request
            // [LOG] Log security exception for debugging
            android.util.Log.e("LocationUtils", "Permission denied during location request", e)
            null  // [RETURN] Return null on permission error
            
        } catch (e: Exception) {
            // [ERROR HANDLING] Catch all other exceptions (network, GPS disabled, etc.)
            // [LOG] Log error for debugging
            android.util.Log.e("LocationUtils", "Error getting location", e)
            null  // [RETURN] Return null on any error
        }
    }
    
    /**
     * [METHOD] Check if location permission is granted
     * [RETURNS] true if ACCESS_FINE_LOCATION granted, false otherwise
     * [PERMISSION] Checks runtime permission status
     */
    fun hasLocationPermission(): Boolean {
        // [PERMISSION CHECK] Check if FINE_LOCATION permission granted
        // [RUNTIME] Checks current permission status (can change at any time)
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    /**
     * [METHOD] Get approximate location (coarse)
     * [RETURNS] Location object with approximate lat/long or null
     * [ACCURACY] Lower accuracy - uses WiFi and cell towers only (no GPS)
     * [BATTERY] More battery-efficient than fine location
     * [USE CASE] When precise location not needed
     */
    suspend fun getApproximateLocation(): Location? {
        // [PERMISSION CHECK] Check for COARSE_LOCATION permission
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null  // [NO PERMISSION] Return null if not granted
        }
        
        val cancellationTokenSource = CancellationTokenSource()
        
        return try {
            // [LOCATION REQUEST] Request with balanced accuracy
            // [PRIORITY] PRIORITY_BALANCED_POWER_ACCURACY - WiFi + Cell (no GPS)
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,  // [ACCURACY] ~100m accuracy
                cancellationTokenSource.token
            ).await()
            
        } catch (e: Exception) {
            android.util.Log.e("LocationUtils", "Error getting approximate location", e)
            null
        }
    }
    
    /**
     * [METHOD] Anonymize location coordinates
     * [PURPOSE] Round coordinates to protect user privacy in anonymous mode
     * [PARAMS] location - Original precise location
     * [RETURNS] Location with rounded coordinates (~111m accuracy)
     * [PRIVACY] Prevents exact address identification
     */
    fun anonymizeLocation(location: Location): Location {
        // [ANONYMIZATION] Round to 3 decimal places (~111 meters accuracy)
        // [PRIVACY] Prevents pinpointing exact house/building
        // [EXAMPLE] 12.9715987 -> 12.972
        val anonymizedLocation = Location(location)
        anonymizedLocation.latitude = roundToDecimals(location.latitude, 3)
        anonymizedLocation.longitude = roundToDecimals(location.longitude, 3)
        return anonymizedLocation
    }
    
    /**
     * [HELPER] Round double to specified decimal places
     * [PARAMS] value - Number to round
     * [PARAMS] decimals - Number of decimal places
     * [RETURNS] Rounded value
     */
    private fun roundToDecimals(value: Double, decimals: Int): Double {
        val multiplier = Math.pow(10.0, decimals.toDouble())
        return Math.round(value * multiplier) / multiplier
    }
}

/**
 * [USAGE EXAMPLES]
 * 
 * // Check permission before requesting location
 * val locationUtils = LocationUtils(context)
 * if (locationUtils.hasLocationPermission()) {
 *     val location = locationUtils.getCurrentLocation()
 *     // Use location
 * } else {
 *     // Request permission from user
 *     requestLocationPermission()
 * }
 * 
 * // Anonymous mode - use anonymized location
 * val location = locationUtils.getCurrentLocation()
 * if (location != null && isAnonymousMode) {
 *     val anonymized = locationUtils.anonymizeLocation(location)
 *     // Submit anonymized location
 * }
 */
