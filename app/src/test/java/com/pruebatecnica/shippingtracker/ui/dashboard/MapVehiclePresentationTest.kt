package com.pruebatecnica.shippingtracker.ui.dashboard

import com.pruebatecnica.shippingtracker.domain.Vehicle
import org.junit.Assert.assertEquals
import org.junit.Test

class MapVehiclePresentationTest {
    @Test
    fun statusColor_mapsMovingStoppedAndUnknownStatuses() {
        assertEquals(0xFF23B4D9, markerColorForStatus("moving"))
        assertEquals(0xFFF00101, markerColorForStatus("stopped"))
        assertEquals(0xFF9841D1, markerColorForStatus("offline"))
    }

    @Test
    fun markerPresentation_preservesPlateSpeedAndAngle() {
        val marker = Vehicle(
            id = 1,
            plate = "ABC-123",
            speed = 72.5,
            latitude = -12.0464,
            longitude = -77.0428,
            angle = 135.0,
            status = "moving",
        ).toMapMarker(bounds = MapBounds(-13.0, -11.0, -78.0, -76.0))

        assertEquals("ABC-123", marker.plate)
        assertEquals("72.5 km/h", marker.speedLabel)
        assertEquals(135.0, marker.rotationDegrees, 0.0)
        assertEquals(0xFF23B4D9, marker.colorHex)
    }

    @Test
    fun coordinateNormalization_placesVehicleInsideFallbackMap() {
        val bounds = MapBounds(minLatitude = -13.0, maxLatitude = -11.0, minLongitude = -78.0, maxLongitude = -76.0)

        val center = normalizeVehiclePosition(latitude = -12.0, longitude = -77.0, bounds = bounds)
        val northWest = normalizeVehiclePosition(latitude = -11.0, longitude = -78.0, bounds = bounds)
        val southEast = normalizeVehiclePosition(latitude = -13.0, longitude = -76.0, bounds = bounds)

        assertEquals(0.5f, center.xFraction, 0.0f)
        assertEquals(0.5f, center.yFraction, 0.0f)
        assertEquals(0.0f, northWest.xFraction, 0.0f)
        assertEquals(0.0f, northWest.yFraction, 0.0f)
        assertEquals(1.0f, southEast.xFraction, 0.0f)
        assertEquals(1.0f, southEast.yFraction, 0.0f)
    }
}
