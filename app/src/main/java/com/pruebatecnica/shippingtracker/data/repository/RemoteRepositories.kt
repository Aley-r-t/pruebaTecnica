package com.pruebatecnica.shippingtracker.data.repository

import com.pruebatecnica.shippingtracker.data.api.ApiResponseDto
import com.pruebatecnica.shippingtracker.data.api.ApiService
import com.pruebatecnica.shippingtracker.data.api.ForgotPasswordRequestDto
import com.pruebatecnica.shippingtracker.data.api.LoginRequestDto
import com.pruebatecnica.shippingtracker.data.api.NotificationDto
import com.pruebatecnica.shippingtracker.data.api.VehicleDto
import com.pruebatecnica.shippingtracker.data.api.requireToken
import com.pruebatecnica.shippingtracker.data.api.toDomain
import com.pruebatecnica.shippingtracker.data.api.toForgotPasswordResult
import com.pruebatecnica.shippingtracker.domain.AppResult
import com.pruebatecnica.shippingtracker.domain.AuthRepository
import com.pruebatecnica.shippingtracker.domain.AuthToken
import com.pruebatecnica.shippingtracker.domain.ForgotPasswordResult
import com.pruebatecnica.shippingtracker.domain.NotificationItem
import com.pruebatecnica.shippingtracker.domain.NotificationRepository
import com.pruebatecnica.shippingtracker.domain.Vehicle
import com.pruebatecnica.shippingtracker.domain.VehicleRepository

class RemoteAuthRepository(
    private val apiService: ApiService,
) : AuthRepository {
    override suspend fun login(email: String, password: String): AppResult<AuthToken> = runCatching {
        val response = apiService.login(LoginRequestDto(email = email, password = password))
        if (response.success) {
            AppResult.Success(response.requireToken())
        } else {
            AppResult.Error(response.message)
        }
    }.getOrElse { throwable -> AppResult.Error(message = throwable.message ?: "Login failed", cause = throwable) }

    override suspend fun forgotPassword(email: String): AppResult<ForgotPasswordResult> = runCatching {
        val response = apiService.forgotPassword(ForgotPasswordRequestDto(email = email))
        if (response.success) {
            AppResult.Success(response.toForgotPasswordResult())
        } else {
            AppResult.Error(response.message)
        }
    }.getOrElse { throwable -> AppResult.Error(message = throwable.message ?: "Forgot password failed", cause = throwable) }
}

class RemoteVehicleRepository(
    private val apiService: ApiService,
) : VehicleRepository {
    override suspend fun getVehicles(): AppResult<List<Vehicle>> =
        mapApiList(
            fetch = { apiService.getVehicles() },
            mapper = VehicleDto::toDomain,
        )
}

class RemoteNotificationRepository(
    private val apiService: ApiService,
) : NotificationRepository {
    override suspend fun getNotifications(): AppResult<List<NotificationItem>> =
        mapApiList(
            fetch = { apiService.getNotifications() },
            mapper = NotificationDto::toDomain,
        )
}

private suspend fun <Dto, Domain> mapApiList(
    fetch: suspend () -> ApiResponseDto<List<Dto>>,
    mapper: (Dto) -> Domain,
): AppResult<List<Domain>> = runCatching {
    val response = fetch()
    if (response.success) {
        AppResult.Success(response.data.map(mapper))
    } else {
        AppResult.Error(response.message)
    }
}.getOrElse { throwable -> AppResult.Error(message = throwable.message ?: "Request failed", cause = throwable) }
