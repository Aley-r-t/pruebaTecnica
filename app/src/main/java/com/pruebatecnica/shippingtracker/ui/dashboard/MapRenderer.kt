package com.pruebatecnica.shippingtracker.ui.dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

fun interface MapRenderer {
    @Composable
    fun Render(markers: List<MapVehicleMarker>, modifier: Modifier)
}

val GoogleMapsRenderer = MapRenderer { markers, modifier ->
    val center = if (markers.isNotEmpty()) {
        LatLng(markers.map { it.latitude }.average(), markers.map { it.longitude }.average())
    } else {
        LatLng(0.0, 0.0)
    }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(center, 15f)
    }
    GoogleMap(modifier = modifier, cameraPositionState = cameraPositionState) {
        markers.forEach { marker ->
            MarkerComposable(
                keys = arrayOf<Any>(marker.plate, marker.colorHex, marker.rotationDegrees),
                state = MarkerState(position = LatLng(marker.latitude, marker.longitude)),
                title = marker.plate,
                snippet = marker.speedLabel,
            ) {
                VehicleMapMarker(marker = marker)
            }
        }
    }
}

@Composable
private fun VehicleMapMarker(marker: MapVehicleMarker) {
    val color = Color(marker.colorHex)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        TopDownCarIcon(
            color = color,
            rotationDegrees = marker.rotationDegrees.toFloat(),
        )
        Box(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(50))
                .border(1.5.dp, color, RoundedCornerShape(50))
                .padding(horizontal = 8.dp, vertical = 3.dp),
        ) {
            Text(
                text = marker.plate,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )
        }
        Text(
            text = marker.speedLabel,
            fontSize = 10.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun TopDownCarIcon(color: Color, rotationDegrees: Float) {
    val darkGray = Color(0xFF1C1C1C)
    val windowColor = Color(0xFF2A2A2A).copy(alpha = 0.75f)

    Canvas(
        modifier = Modifier
            .size(width = 36.dp, height = 56.dp)
            .rotate(rotationDegrees),
    ) {
        val w = size.width
        val h = size.height

        // Wheels
        val wheelW = w * 0.18f
        val wheelH = h * 0.18f
        val wheelRadius = CornerRadius(wheelW * 0.4f)
        listOf(
            Offset(0f, h * 0.07f),
            Offset(w - wheelW, h * 0.07f),
            Offset(0f, h * 0.75f),
            Offset(w - wheelW, h * 0.75f),
        ).forEach { drawRoundRect(darkGray, it, Size(wheelW, wheelH), wheelRadius) }

        // Car body
        drawRoundRect(
            color = color,
            topLeft = Offset(w * 0.16f, h * 0.04f),
            size = Size(w * 0.68f, h * 0.92f),
            cornerRadius = CornerRadius(w * 0.22f),
        )

        // Front windshield
        drawRoundRect(
            color = windowColor,
            topLeft = Offset(w * 0.22f, h * 0.10f),
            size = Size(w * 0.56f, h * 0.18f),
            cornerRadius = CornerRadius(w * 0.12f),
        )

        // Rear windshield
        drawRoundRect(
            color = windowColor,
            topLeft = Offset(w * 0.22f, h * 0.72f),
            size = Size(w * 0.56f, h * 0.16f),
            cornerRadius = CornerRadius(w * 0.10f),
        )

        // Side windows left
        drawRoundRect(
            color = windowColor,
            topLeft = Offset(w * 0.16f, h * 0.32f),
            size = Size(w * 0.12f, h * 0.36f),
            cornerRadius = CornerRadius(w * 0.06f),
        )

        // Side windows right
        drawRoundRect(
            color = windowColor,
            topLeft = Offset(w * 0.72f, h * 0.32f),
            size = Size(w * 0.12f, h * 0.36f),
            cornerRadius = CornerRadius(w * 0.06f),
        )
    }
}

val CredentialSafeMapRenderer = MapRenderer { markers, modifier ->
    FallbackMap(markers = markers, modifier = modifier)
}
