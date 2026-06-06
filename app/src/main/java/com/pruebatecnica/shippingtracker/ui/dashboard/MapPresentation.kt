package com.pruebatecnica.shippingtracker.ui.dashboard

import com.pruebatecnica.shippingtracker.domain.Vehicle

private const val MovingColorHex: Long = 0xFF23B4D9
private const val StoppedColorHex: Long = 0xFFF00101
private const val UnknownColorHex: Long = 0xFF9841D1

data class MapBounds(
    val minLatitude: Double,
    val maxLatitude: Double,
    val minLongitude: Double,
    val maxLongitude: Double,
)

data class NormalizedMapPosition(
    val xFraction: Float,
    val yFraction: Float,
)

data class MapVehicleMarker(
    val plate: String,
    val speedLabel: String,
    val xFraction: Float,
    val yFraction: Float,
    val rotationDegrees: Double,
    val colorHex: Long,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
)

fun markerColorForStatus(status: String): Long = when (status.lowercase()) {
    "green", "moving", "active", "online" -> MovingColorHex
    "red", "stopped", "inactive", "alert" -> StoppedColorHex
    else -> UnknownColorHex
}

fun Vehicle.toMapMarker(bounds: MapBounds = defaultBoundsFor(listOf(this))): MapVehicleMarker {
    val position = normalizeVehiclePosition(latitude = latitude, longitude = longitude, bounds = bounds)
    return MapVehicleMarker(
        plate = plate,
        speedLabel = speed,
        xFraction = position.xFraction,
        yFraction = position.yFraction,
        rotationDegrees = angle,
        colorHex = markerColorForStatus(status),
        latitude = latitude,
        longitude = longitude,
    )
}

fun List<Vehicle>.toMapMarkers(): List<MapVehicleMarker> {
    val bounds = defaultBoundsFor(this)
    return map { vehicle -> vehicle.toMapMarker(bounds) }
}

fun normalizeVehiclePosition(
    latitude: Double,
    longitude: Double,
    bounds: MapBounds,
): NormalizedMapPosition {
    val longitudeRange = (bounds.maxLongitude - bounds.minLongitude).takeUnless { it == 0.0 } ?: 1.0
    val latitudeRange = (bounds.maxLatitude - bounds.minLatitude).takeUnless { it == 0.0 } ?: 1.0
    val x = ((longitude - bounds.minLongitude) / longitudeRange).toFloat().coerceIn(0f, 1f)
    val y = ((bounds.maxLatitude - latitude) / latitudeRange).toFloat().coerceIn(0f, 1f)
    return NormalizedMapPosition(xFraction = x, yFraction = y)
}

private fun defaultBoundsFor(vehicles: List<Vehicle>): MapBounds {
    if (vehicles.isEmpty()) {
        return MapBounds(-90.0, 90.0, -180.0, 180.0)
    }
    val latitudes = vehicles.map { it.latitude }
    val longitudes = vehicles.map { it.longitude }
    return MapBounds(
        minLatitude = latitudes.minOrNull() ?: -90.0,
        maxLatitude = latitudes.maxOrNull() ?: 90.0,
        minLongitude = longitudes.minOrNull() ?: -180.0,
        maxLongitude = longitudes.maxOrNull() ?: 180.0,
    ).withPaddingForSinglePoint()
}

private fun MapBounds.withPaddingForSinglePoint(): MapBounds {
    val latitudePadding = if (minLatitude == maxLatitude) 0.01 else 0.0
    val longitudePadding = if (minLongitude == maxLongitude) 0.01 else 0.0
    return copy(
        minLatitude = minLatitude - latitudePadding,
        maxLatitude = maxLatitude + latitudePadding,
        minLongitude = minLongitude - longitudePadding,
        maxLongitude = maxLongitude + longitudePadding,
    )
}

