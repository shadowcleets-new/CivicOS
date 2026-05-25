package com.nivar.app.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any

@ExperimentalCoroutinesApi
class LocationUtilsTest {

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockFusedLocationClient: FusedLocationProviderClient

    private lateinit var locationUtils: LocationUtils

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)

        // Mock LocationServices to return our mock client
        Mockito.mockStatic(LocationServices::class.java).use { mockedLocationServices ->
            mockedLocationServices.`when`<FusedLocationProviderClient> {
                LocationServices.getFusedLocationProviderClient(mockContext)
            }.thenReturn(mockFusedLocationClient)

            locationUtils = LocationUtils(mockContext)
        }
    }

    @Test
    fun getApproximateLocation_permissionDenied_returnsNull() = runTest {
        Mockito.mockStatic(ContextCompat::class.java).use { mockedContextCompat ->
            mockedContextCompat.`when`<Int> {
                ContextCompat.checkSelfPermission(mockContext, Manifest.permission.ACCESS_COARSE_LOCATION)
            }.thenReturn(PackageManager.PERMISSION_DENIED)

            val result = locationUtils.getApproximateLocation()
            assertNull(result)
        }
    }

    @Test
    fun getApproximateLocation_exceptionThrown_returnsNull() = runTest {
        // Mock permission granted
        Mockito.mockStatic(ContextCompat::class.java).use { mockedContextCompat ->
            mockedContextCompat.`when`<Int> {
                ContextCompat.checkSelfPermission(mockContext, Manifest.permission.ACCESS_COARSE_LOCATION)
            }.thenReturn(PackageManager.PERMISSION_GRANTED)

            Mockito.mockStatic(android.util.Log::class.java).use { mockedLog ->
                mockedLog.`when`<Int> {
                    android.util.Log.e(any(), any(), any())
                }.thenReturn(0)

                // Setup mock to throw exception when getCurrentLocation is called
                `when`(mockFusedLocationClient.getCurrentLocation(any<Int>(), any())).thenThrow(SecurityException("Mock security exception"))

                // Call the method under test
                val result = locationUtils.getApproximateLocation()

                // Verify it returns null rather than crashing
                assertNull(result)
            }
        }
    }
}
