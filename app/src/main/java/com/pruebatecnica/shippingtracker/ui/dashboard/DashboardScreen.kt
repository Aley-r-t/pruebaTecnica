package com.pruebatecnica.shippingtracker.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pruebatecnica.shippingtracker.ui.theme.ShippingTrackerTheme

@Composable
fun DashboardScreen(
    state: DashboardUiState,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    mapRenderer: MapRenderer = GoogleMapsRenderer,
) {
    mapRenderer.Render(
        markers = state.markers,
        modifier = modifier.fillMaxSize(),
    )
}

@Composable
fun FallbackMap(markers: List<MapVehicleMarker>, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(Color(0xFFEAF7FB)),
    ) {
        Text(
            text = "Mapa no disponible — se requieren credenciales de mapa",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp),
        )
        markers.forEach { marker ->
            VehicleMarker(marker = marker)
        }
    }
}

@Composable
private fun VehicleMarker(marker: MapVehicleMarker) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = (marker.xFraction * 220).dp,
                top = (marker.yFraction * 260).dp,
            ),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .rotate(marker.rotationDegrees.toFloat())
                    .background(Color(marker.colorHex), CircleShape),
            )
            Column {
                Text(text = marker.plate, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurface)
                Text(text = marker.speedLabel, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DashboardScreenPreview() {
    ShippingTrackerTheme {
        DashboardScreen(
            state = DashboardUiState(
                markers = listOf(MapVehicleMarker("ABC-123", "72.5 km/h", 0.5f, 0.5f, 90.0, 0xFF23B4D9)),
            ),
            onRefresh = {},
        )
    }
}
