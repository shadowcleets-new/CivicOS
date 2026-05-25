package com.nivar.app.utils

import android.location.Location
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class LocationUtilsTest {

    private lateinit var locationUtils: LocationUtils

    @Before
    fun setUp() {
        locationUtils = LocationUtils(RuntimeEnvironment.getApplication())
    }

    @Test
    fun anonymizeLocation_positiveCoordinates_roundsToThreeDecimals() {
        val original = Location("test_provider")
        original.latitude = 12.9715987
        original.longitude = 77.5945627

        val anonymized = locationUtils.anonymizeLocation(original)

        assertEquals(12.972, anonymized.latitude, 0.0)
        assertEquals(77.595, anonymized.longitude, 0.0)
    }

    @Test
    fun anonymizeLocation_negativeCoordinates_roundsToThreeDecimals() {
        val original = Location("test_provider")
        original.latitude = -12.9715987
        original.longitude = -77.5945627

        val anonymized = locationUtils.anonymizeLocation(original)

        assertEquals(-12.972, anonymized.latitude, 0.0)
        assertEquals(-77.595, anonymized.longitude, 0.0)
    }

    @Test
    fun anonymizeLocation_zeroCoordinates_remainsZero() {
        val original = Location("test_provider")
        original.latitude = 0.0
        original.longitude = 0.0

        val anonymized = locationUtils.anonymizeLocation(original)

        assertEquals(0.0, anonymized.latitude, 0.0)
        assertEquals(0.0, anonymized.longitude, 0.0)
    }

    @Test
    fun anonymizeLocation_exactThreeDecimals_remainsSame() {
        val original = Location("test_provider")
        original.latitude = 12.123
        original.longitude = -77.456

        val anonymized = locationUtils.anonymizeLocation(original)

        assertEquals(12.123, anonymized.latitude, 0.0)
        assertEquals(-77.456, anonymized.longitude, 0.0)
    }

    @Test
    fun anonymizeLocation_lessThanThreeDecimals_remainsSame() {
        val original = Location("test_provider")
        original.latitude = 12.1
        original.longitude = -77.45

        val anonymized = locationUtils.anonymizeLocation(original)

        assertEquals(12.1, anonymized.latitude, 0.0)
        assertEquals(-77.45, anonymized.longitude, 0.0)
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
