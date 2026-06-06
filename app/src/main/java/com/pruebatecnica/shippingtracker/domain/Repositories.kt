package com.pruebatecnica.shippingtracker.domain

interface AuthRepository {
    suspend fun login(email: String, password: String): AppResult<AuthToken>
    suspend fun forgotPassword(email: String): AppResult<ForgotPasswordResult>
}

interface VehicleRepository {
    suspend fun getVehicles(): AppResult<List<Vehicle>>
}

interface NotificationRepository {
    suspend fun getNotifications(): AppResult<List<NotificationItem>>
}
