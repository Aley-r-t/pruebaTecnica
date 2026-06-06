package com.pruebatecnica.shippingtracker.ui.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pruebatecnica.shippingtracker.domain.NotificationItem
import com.pruebatecnica.shippingtracker.ui.theme.Cyan
import com.pruebatecnica.shippingtracker.ui.theme.DangerRed
import com.pruebatecnica.shippingtracker.ui.theme.Purple
import com.pruebatecnica.shippingtracker.ui.theme.ShippingTrackerTheme
import com.pruebatecnica.shippingtracker.ui.theme.ShippingWhite

@Composable
fun NotificationsScreen(
    state: NotificationsUiState,
    onRefresh: () -> Unit,
    onDelete: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ShippingWhite)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(state.notifications, key = { it.id }) { notification ->
            NotificationCard(
                notification = notification,
                onDelete = { onDelete(notification.id) },
            )
        }
    }
}

@Composable
private fun NotificationCard(notification: NotificationItem, onDelete: () -> Unit) {
    val gradientBrush = Brush.linearGradient(colors = listOf(Cyan, Purple))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, gradientBrush, RoundedCornerShape(16.dp))
            .background(ShippingWhite, RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "Compra: ${notification.invoiceNumber}",
                color = Cyan,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Description,
                    contentDescription = null,
                    tint = Cyan,
                    modifier = Modifier.size(16.dp),
                )
                Text(
                    text = notification.status,
                    color = Cyan,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Schedule,
                    contentDescription = null,
                    tint = Cyan,
                    modifier = Modifier.size(16.dp),
                )
                Text(
                    text = notification.createdAt,
                    color = Cyan,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(DangerRed)
                .clickable(onClick = onDelete),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Eliminar",
                tint = ShippingWhite,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationsScreenPreview() {
    ShippingTrackerTheme {
        NotificationsScreen(
            state = NotificationsUiState(
                notifications = listOf(
                    NotificationItem(1, "001-000001", "En Origen", "2026-04-02 08:00:00"),
                    NotificationItem(2, "001-000001", "En Tránsito", "2026-04-02 08:00:00"),
                    NotificationItem(3, "001-000001", "En Domicilio", "2026-04-02 08:00:00"),
                    NotificationItem(4, "001-000001", "Entregado", "2026-04-02 08:00:00"),
                ),
            ),
            onRefresh = {},
            onDelete = {},
        )
    }
}
