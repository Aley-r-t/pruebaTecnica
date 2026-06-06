package com.pruebatecnica.shippingtracker.data.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): LoginResponseDto

    @POST("auth/forgot")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequestDto): ForgotPasswordResponseDto

    @GET("vehicles")
    suspend fun getVehicles(): ApiResponseDto<List<VehicleDto>>

    @GET("notifications")
    suspend fun getNotifications(): ApiResponseDto<List<NotificationDto>>
}
