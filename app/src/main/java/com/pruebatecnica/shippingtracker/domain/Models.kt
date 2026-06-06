package com.pruebatecnica.shippingtracker.domain

data class AuthToken(
    val value: String,
    val expiresAt: String,
)

data class ForgotPasswordResult(
    val success: Boolean,
    val message: String,
)

data class Vehicle(
    val id: Int,
    val plate: String,
    val speed: String,
    val latitude: Double,
    val longitude: Double,
    val angle: Double,
    val status: String,
)

data class NotificationItem(
    val id: Int,
    val invoiceNumber: String,
    val status: String,
    val createdAt: String,
)

sealed interface AppResult<out T> {
    data class Success<T>(val value: T) : AppResult<T>
    data class Error(val message: String, val cause: Throwable? = null) : AppResult<Nothing>
}
