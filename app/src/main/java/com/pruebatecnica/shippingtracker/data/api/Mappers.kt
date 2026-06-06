package com.pruebatecnica.shippingtracker.data.api

import com.pruebatecnica.shippingtracker.domain.AuthToken
import com.pruebatecnica.shippingtracker.domain.ForgotPasswordResult
import com.pruebatecnica.shippingtracker.domain.NotificationItem
import com.pruebatecnica.shippingtracker.domain.Vehicle

fun LoginResponseDto.toAuthTokenOrNull(): AuthToken? = data?.let { loginData ->
    AuthToken(value = loginData.tokenValue, expiresAt = loginData.tokenExpired)
}

fun LoginResponseDto.requireToken(): AuthToken = requireNotNull(toAuthTokenOrNull()) {
    "Login response does not contain token data"
}

fun ForgotPasswordResponseDto.toForgotPasswordResult(): ForgotPasswordResult =
    ForgotPasswordResult(success = success, message = message)

fun VehicleDto.toDomain(): Vehicle = Vehicle(
    id = id,
    plate = plate,
    speed = speed,
    latitude = latitude,
    longitude = longitude,
    angle = angle,
    status = status,
)

fun NotificationDto.toDomain(): NotificationItem = NotificationItem(
    id = id,
    invoiceNumber = invoiceNumber,
    status = status,
    createdAt = createdAt,
)
