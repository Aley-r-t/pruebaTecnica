package com.pruebatecnica.shippingtracker.regression

import com.pruebatecnica.shippingtracker.data.api.LoginDataDto
import com.pruebatecnica.shippingtracker.data.api.LoginResponseDto
import com.pruebatecnica.shippingtracker.data.api.NotificationDto
import com.pruebatecnica.shippingtracker.data.api.VehicleDto
import com.pruebatecnica.shippingtracker.data.api.requireToken
import com.pruebatecnica.shippingtracker.data.api.toDomain
import com.pruebatecnica.shippingtracker.domain.Vehicle
import com.pruebatecnica.shippingtracker.ui.dashboard.MapBounds
import com.pruebatecnica.shippingtracker.ui.dashboard.markerColorForStatus
import com.pruebatecnica.shippingtracker.ui.dashboard.normalizeVehiclePosition
import com.pruebatecnica.shippingtracker.ui.dashboard.toMapMarker
import com.pruebatecnica.shippingtracker.ui.navigation.AppRoute
import com.pruebatecnica.shippingtracker.ui.navigation.authenticatedBottomRoutes
import com.pruebatecnica.shippingtracker.ui.theme.CyanHex
import com.pruebatecnica.shippingtracker.ui.theme.DangerRedHex
import com.pruebatecnica.shippingtracker.ui.theme.PrimaryBlueHex
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FinalRegressionTest {
    @Test
    fun assignmentPalette_containsRequiredBrandColors() {
        assertEquals(0xFF0041BA, PrimaryBlueHex)
        assertEquals(0xFF23B4D9, CyanHex)
        assertEquals(0xFFF00101, DangerRedHex)
    }

    @Test
    fun apiContracts_preserveCriticalAssignmentJsonFieldsThroughDomainMapping() {
        val token = LoginResponseDto(true, "ok", LoginDataDto("jwt", "2026-12-31")).requireToken()
        val vehicle = VehicleDto(1, "ABC-123", 45.0, -12.0, -77.0, 180.0, "moving").toDomain()
        val notification = NotificationDto(2, "INV-002", "pending", "2026-06-05").toDomain()

        assertEquals("jwt", token.value)
        assertEquals("2026-12-31", token.expiresAt)
        assertEquals("ABC-123", vehicle.plate)
        assertEquals(180.0, vehicle.angle, 0.0)
        assertEquals("INV-002", notification.invoiceNumber)
        assertEquals("2026-06-05", notification.createdAt)
    }

    @Test
    fun mapFallback_preservesVehicleStatusPositionRotationAndLabels() {
        val vehicle = Vehicle(1, "ABC-123", 45.0, -12.0, -77.0, 180.0, "moving")
        val marker = vehicle.toMapMarker(MapBounds(-13.0, -11.0, -78.0, -76.0))
        val position = normalizeVehiclePosition(-12.0, -77.0, MapBounds(-13.0, -11.0, -78.0, -76.0))

        assertEquals(0xFF23B4D9, markerColorForStatus("moving"))
        assertEquals("ABC-123", marker.plate)
        assertEquals("45.0 km/h", marker.speedLabel)
        assertEquals(180.0, marker.rotationDegrees, 0.0)
        assertEquals(0.5f, position.xFraction, 0.0f)
        assertEquals(0.5f, position.yFraction, 0.0f)
    }

    @Test
    fun authenticatedShell_exposesRequiredBottomNavigationDestinations() {
        val routes = authenticatedBottomRoutes()

        assertEquals(listOf(AppRoute.Dashboard, AppRoute.Billings, AppRoute.Profile, AppRoute.Notifications), routes)
        assertTrue(routes.all { route -> route.showInBottomBar })
    }
}
