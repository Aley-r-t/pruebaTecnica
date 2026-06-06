package com.pruebatecnica.shippingtracker.ui.shell

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pruebatecnica.shippingtracker.ui.dashboard.DashboardScreen
import com.pruebatecnica.shippingtracker.ui.dashboard.DashboardUiState
import com.pruebatecnica.shippingtracker.ui.navigation.AppRoute
import com.pruebatecnica.shippingtracker.ui.navigation.authenticatedBottomRoutes
import com.pruebatecnica.shippingtracker.ui.notifications.NotificationsScreen
import com.pruebatecnica.shippingtracker.ui.notifications.NotificationsUiState
import com.pruebatecnica.shippingtracker.ui.theme.Cyan
import com.pruebatecnica.shippingtracker.ui.theme.PrimaryBlue
import com.pruebatecnica.shippingtracker.ui.theme.Purple
import com.pruebatecnica.shippingtracker.ui.theme.ShippingWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticatedAppShell(
    selectedRoute: AppRoute,
    onRouteSelected: (AppRoute) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    dashboardState: DashboardUiState = DashboardUiState(),
    notificationsState: NotificationsUiState = NotificationsUiState(),
    onDashboardRefresh: () -> Unit = {},
    onNotificationsRefresh: () -> Unit = {},
    onNotificationDelete: (Int) -> Unit = {},
) {
    val headerColor = if (selectedRoute == AppRoute.Dashboard) Cyan else PrimaryBlue

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = selectedRoute.title,
                        color = ShippingWhite,
                        fontWeight = FontWeight.Bold,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = headerColor,
                ),
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar sesión",
                            tint = ShippingWhite,
                        )
                    }
                },
            )
        },
        bottomBar = {
            CustomBottomNav(
                selectedRoute = selectedRoute,
                onRouteSelected = onRouteSelected,
            )
        },
    ) { innerPadding ->
        when (selectedRoute) {
            AppRoute.Dashboard -> DashboardScreen(
                state = dashboardState,
                onRefresh = onDashboardRefresh,
                modifier = Modifier.padding(innerPadding),
            )
            AppRoute.Billings -> BillingsScreen(modifier = Modifier.padding(innerPadding))
            AppRoute.Profile -> ProfileScreen(modifier = Modifier.padding(innerPadding))
            AppRoute.Notifications -> NotificationsScreen(
                state = notificationsState,
                onRefresh = onNotificationsRefresh,
                onDelete = onNotificationDelete,
                modifier = Modifier.padding(innerPadding),
            )
            else -> DashboardScreen(
                state = dashboardState,
                onRefresh = onDashboardRefresh,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

@Composable
private fun CustomBottomNav(
    selectedRoute: AppRoute,
    onRouteSelected: (AppRoute) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
    ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Cyan, RoundedCornerShape(28.dp))
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        authenticatedBottomRoutes().forEach { route ->
            val isSelected = route == selectedRoute
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = if (isSelected) Purple else Cyan,
                        shape = RoundedCornerShape(12.dp),
                    )
                    .clickable { onRouteSelected(route) },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = route.navIcon(),
                    contentDescription = route.title,
                    tint = ShippingWhite,
                    modifier = Modifier.size(26.dp),
                )
            }
        }
    }
    }
}

private fun AppRoute.navIcon(): ImageVector = when (this) {
    AppRoute.Dashboard -> Icons.Filled.LocalShipping
    AppRoute.Billings -> Icons.Filled.Receipt
    AppRoute.Profile -> Icons.Filled.Person
    AppRoute.Notifications -> Icons.Filled.Notifications
    else -> Icons.Filled.LocalShipping
}
