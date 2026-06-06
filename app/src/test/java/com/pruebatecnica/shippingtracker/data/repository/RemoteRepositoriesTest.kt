package com.pruebatecnica.shippingtracker.data.repository

import com.pruebatecnica.shippingtracker.data.api.ApiResponseDto
import com.pruebatecnica.shippingtracker.data.api.ApiService
import com.pruebatecnica.shippingtracker.data.api.ForgotPasswordRequestDto
import com.pruebatecnica.shippingtracker.data.api.ForgotPasswordResponseDto
import com.pruebatecnica.shippingtracker.data.api.LoginDataDto
import com.pruebatecnica.shippingtracker.data.api.LoginRequestDto
import com.pruebatecnica.shippingtracker.data.api.LoginResponseDto
import com.pruebatecnica.shippingtracker.data.api.NotificationDto
import com.pruebatecnica.shippingtracker.data.api.VehicleDto
import com.pruebatecnica.shippingtracker.domain.AppResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RemoteRepositoriesTest {
    @Test
    fun authRepository_loginSuccessStoresTokenValueAndExpiry() = runTest {
        val api = FakeApiService(
            loginResponse = LoginResponseDto(
                success = true,
                message = "ok",
                data = LoginDataDto(tokenValue = "jwt-value", tokenExpired = "2026-12-31"),
            ),
        )
        val repository = RemoteAuthRepository(api)

        val result = repository.login(email = "user@example.com", password = "secret")

        assertEquals(LoginRequestDto("user@example.com", "secret"), api.lastLoginRequest)
        assertEquals(AppResult.Success::class, result::class)
        val token = (result as AppResult.Success).value
        assertEquals("jwt-value", token.value)
        assertEquals("2026-12-31", token.expiresAt)
    }

    @Test
    fun authRepository_loginFailureReturnsApiMessage() = runTest {
        val repository = RemoteAuthRepository(
            FakeApiService(loginResponse = LoginResponseDto(success = false, message = "Invalid credentials", data = null)),
        )

        val result = repository.login(email = "bad@example.com", password = "wrong")

        assertEquals(AppResult.Error::class, result::class)
        assertEquals("Invalid credentials", (result as AppResult.Error).message)
    }

    @Test
    fun authRepository_forgotPasswordReturnsSuccessMessage() = runTest {
        val api = FakeApiService(
            forgotResponse = ForgotPasswordResponseDto(success = true, message = "Reset sent", data = null),
        )
        val repository = RemoteAuthRepository(api)

        val result = repository.forgotPassword("user@example.com")

        assertEquals(ForgotPasswordRequestDto("user@example.com"), api.lastForgotRequest)
        assertEquals(AppResult.Success::class, result::class)
        assertEquals("Reset sent", (result as AppResult.Success).value.message)
    }

    @Test
    fun vehicleRepository_mapsApiVehiclesToDomainList() = runTest {
        val repository = RemoteVehicleRepository(
            FakeApiService(
                vehiclesResponse = ApiResponseDto(
                    success = true,
                    message = "ok",
                    data = listOf(
                        VehicleDto(1, "AAA-111", 10.0, -12.0, -77.0, 45.0, "moving"),
                        VehicleDto(2, "BBB-222", 0.0, -13.0, -78.0, 0.0, "stopped"),
                    ),
                ),
            ),
        )

        val result = repository.getVehicles()

        assertTrue(result is AppResult.Success)
        val vehicles = (result as AppResult.Success).value
        assertEquals(2, vehicles.size)
        assertEquals("AAA-111", vehicles[0].plate)
        assertEquals("stopped", vehicles[1].status)
    }

    @Test
    fun notificationRepository_mapsApiNotificationsToDomainList() = runTest {
        val repository = RemoteNotificationRepository(
            FakeApiService(
                notificationsResponse = ApiResponseDto(
                    success = true,
                    message = "ok",
                    data = listOf(NotificationDto(3, "INV-003", "paid", "2026-06-05")),
                ),
            ),
        )

        val result = repository.getNotifications()

        assertTrue(result is AppResult.Success)
        val notifications = (result as AppResult.Success).value
        assertEquals(1, notifications.size)
        assertEquals("INV-003", notifications.single().invoiceNumber)
        assertEquals("paid", notifications.single().status)
    }
}

private class FakeApiService(
    private val loginResponse: LoginResponseDto = LoginResponseDto(false, "not configured", null),
    private val forgotResponse: ForgotPasswordResponseDto = ForgotPasswordResponseDto(false, "not configured", null),
    private val vehiclesResponse: ApiResponseDto<List<VehicleDto>> = ApiResponseDto(false, "not configured", emptyList()),
    private val notificationsResponse: ApiResponseDto<List<NotificationDto>> = ApiResponseDto(false, "not configured", emptyList()),
) : ApiService {
    var lastLoginRequest: LoginRequestDto? = null
    var lastForgotRequest: ForgotPasswordRequestDto? = null

    override suspend fun login(request: LoginRequestDto): LoginResponseDto {
        lastLoginRequest = request
        return loginResponse
    }

    override suspend fun forgotPassword(request: ForgotPasswordRequestDto): ForgotPasswordResponseDto {
        lastForgotRequest = request
        return forgotResponse
    }

    override suspend fun getVehicles(): ApiResponseDto<List<VehicleDto>> = vehiclesResponse

    override suspend fun getNotifications(): ApiResponseDto<List<NotificationDto>> = notificationsResponse
}
