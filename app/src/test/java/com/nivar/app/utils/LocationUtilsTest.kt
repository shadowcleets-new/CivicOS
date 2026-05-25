package com.nivar.app.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LocationUtilsTest {

    private lateinit var context: Context
    private lateinit var locationUtils: LocationUtils
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @Before
    fun setUp() {
        context = mockk()
        fusedLocationClient = mockk()

        mockkStatic(ContextCompat::class)
        mockkStatic(LocationServices::class)

        every { LocationServices.getFusedLocationProviderClient(context) } returns fusedLocationClient

        locationUtils = LocationUtils(context)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun hasLocationPermission_whenGranted_returnsTrue() {
        every {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } returns PackageManager.PERMISSION_GRANTED

        assertTrue(locationUtils.hasLocationPermission())
    }

    @Test
    fun hasLocationPermission_whenDenied_returnsFalse() {
        every {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } returns PackageManager.PERMISSION_DENIED

        assertFalse(locationUtils.hasLocationPermission())
    }
}
