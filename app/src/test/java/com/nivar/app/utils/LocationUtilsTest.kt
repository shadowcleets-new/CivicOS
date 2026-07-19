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
    }
}
