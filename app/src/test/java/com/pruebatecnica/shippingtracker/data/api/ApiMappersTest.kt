package com.pruebatecnica.shippingtracker.data.api

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import kotlinx.serialization.json.Json

class ApiMappersTest {
    @Test
    fun loginResponse_mapsTokenValueAndExpiryToDomain() {
        val response = LoginResponseDto(
            success = true,
            message = "ok",
            data = LoginDataDto(
                tokenValue = "token-123",
                tokenExpired = "2026-12-31T23:59:59Z",
            ),
        )

        val token = response.requireToken()

        assertEquals("token-123", token.value)
        assertEquals("2026-12-31T23:59:59Z", token.expiresAt)
    }

    @Test
    fun loginResponse_withoutDataMapsToNullToken() {
        val response = LoginResponseDto(success = false, message = "Unauthorized", data = null)

        assertNull(response.toAuthTokenOrNull())
    }

    @Test
    fun forgotPasswordResponse_mapsMessageAndSuccessFlag() {
        val response = ForgotPasswordResponseDto(success = true, message = "Email sent", data = null)

        val result = response.toForgotPasswordResult()

        assertEquals(true, result.success)
        assertEquals("Email sent", result.message)
    }

    @Test
    fun vehicleResponse_mapsAllPositionAndStatusFields() {
        val dto = VehicleDto(
            id = 7,
            plate = "ABC-123",
            speed = 65.5,
            latitude = -12.0464,
            longitude = -77.0428,
            angle = 135.0,
            status = "moving",
        )

        val vehicle = dto.toDomain()

        assertEquals(7, vehicle.id)
        assertEquals("ABC-123", vehicle.plate)
        assertEquals(65.5, vehicle.speed, 0.001)
        assertEquals(-12.0464, vehicle.latitude, 0.0)
        assertEquals(-77.0428, vehicle.longitude, 0.0)
        assertEquals(135.0, vehicle.angle, 0.0)
        assertEquals("moving", vehicle.status)
    }

    @Test
    fun vehiclesResponse_decodesLiveApiSpeedLabels() {
        val json = """
            {
              "success": true,
              "message": "Lista de Vehículos",
              "data": [
                {
                  "id": 1,
                  "plate": "DMO-123",
                  "speed": 80,
                  "latitude": -12.059482,
                  "longitude": -77.032028,
                  "angle": 30,
                  "status": "green"
                }
              ]
            }
        """.trimIndent()

        val response = Json.decodeFromString<ApiResponseDto<List<VehicleDto>>>(json)
        val vehicle = response.data.single().toDomain()

        assertEquals("DMO-123", vehicle.plate)
        assertEquals(80.0, vehicle.speed, 0.001)
        assertEquals(-12.059482, vehicle.latitude, 0.0)
        assertEquals("green", vehicle.status)
    }

    @Test
    fun notificationResponse_mapsInvoiceStatusAndCreatedDate() {
        val dto = NotificationDto(
            id = 42,
            invoiceNumber = "INV-2026-001",
            status = "pending",
            createdAt = "2026-06-05T10:00:00Z",
        )

        val notification = dto.toDomain()

        assertEquals(42, notification.id)
        assertEquals("INV-2026-001", notification.invoiceNumber)
        assertEquals("pending", notification.status)
        assertEquals("2026-06-05T10:00:00Z", notification.createdAt)
    }
}
