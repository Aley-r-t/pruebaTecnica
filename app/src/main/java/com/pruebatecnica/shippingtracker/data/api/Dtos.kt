package com.pruebatecnica.shippingtracker.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponseDto<T>(
    val success: Boolean,
    val message: String,
    val data: T,
)

@Serializable
data class LoginRequestDto(
    val email: String,
    val password: String,
)

@Serializable
data class ForgotPasswordRequestDto(
    val email: String,
)

@Serializable
data class LoginResponseDto(
    val success: Boolean,
    val message: String,
    val data: LoginDataDto?,
)

@Serializable
data class LoginDataDto(
    @SerialName("token_value")
    val tokenValue: String,
    @SerialName("token_expired")
    val tokenExpired: String,
)

@Serializable
data class ForgotPasswordResponseDto(
    val success: Boolean,
    val message: String,
    val data: String?,
)

@Serializable
data class VehicleDto(
    val id: Int,
    val plate: String,
    val speed: String,
    val latitude: Double,
    val longitude: Double,
    val angle: Double,
    val status: String,
)

@Serializable
data class NotificationDto(
    val id: Int,
    @SerialName("invoice_number")
    val invoiceNumber: String,
    val status: String,
    @SerialName("created_at")
    val createdAt: String,
)
