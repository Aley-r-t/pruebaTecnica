package com.pruebatecnica.shippingtracker.data.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

private const val BaseUrl = "https://demoapp2026.alwaysdata.net/api/v1/"

fun createJson(): Json = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
}

fun createApiService(
    baseUrl: String = BaseUrl,
    okHttpClient: OkHttpClient = OkHttpClient.Builder().build(),
    json: Json = createJson(),
): ApiService = Retrofit.Builder()
    .baseUrl(baseUrl)
    .client(okHttpClient)
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .build()
    .create(ApiService::class.java)
