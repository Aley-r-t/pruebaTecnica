package com.pruebatecnica.shippingtracker.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.pruebatecnica.shippingtracker.data.api.createApiService
import com.pruebatecnica.shippingtracker.data.repository.RemoteNotificationRepository
import com.pruebatecnica.shippingtracker.data.repository.RemoteVehicleRepository
import com.pruebatecnica.shippingtracker.ui.auth.ForgotPasswordUiState
import com.pruebatecnica.shippingtracker.ui.auth.ForgotPasswordScreen
import com.pruebatecnica.shippingtracker.ui.auth.LoginScreen
import com.pruebatecnica.shippingtracker.ui.auth.LoginUiState
import com.pruebatecnica.shippingtracker.ui.auth.SplashScreen
import com.pruebatecnica.shippingtracker.ui.dashboard.DashboardViewModel
import com.pruebatecnica.shippingtracker.ui.navigation.AppRoute
import com.pruebatecnica.shippingtracker.ui.notifications.NotificationsUiState
import com.pruebatecnica.shippingtracker.ui.notifications.NotificationsViewModel
import com.pruebatecnica.shippingtracker.ui.shell.AuthenticatedAppShell
import kotlinx.coroutines.delay

@Composable
fun ShippingTrackerApp(modifier: Modifier = Modifier) {
    val apiService = remember { createApiService() }

    val dashboardViewModel: DashboardViewModel = viewModel(
        factory = viewModelFactory {
            initializer { DashboardViewModel(RemoteVehicleRepository(apiService)) }
        },
    )
    val notificationsViewModel: NotificationsViewModel = viewModel(
        factory = viewModelFactory {
            initializer { NotificationsViewModel(RemoteNotificationRepository(apiService)) }
        },
    )

    val dashboardState by dashboardViewModel.uiState.collectAsState()
    val notificationsState by notificationsViewModel.uiState.collectAsState()

    var currentRoute by remember { mutableStateOf<AppRoute>(AppRoute.Splash) }
    var loginState by remember { mutableStateOf(LoginUiState()) }
    var forgotState by remember { mutableStateOf(ForgotPasswordUiState()) }
    var selectedBottomRoute by remember { mutableStateOf(AppRoute.Dashboard) }

    when (currentRoute) {
        AppRoute.Splash -> {
            LaunchedEffect(Unit) {
                delay(2000)
                currentRoute = AppRoute.Login
            }
            SplashScreen(modifier = modifier)
        }

        AppRoute.Login -> {
            LoginScreen(
                state = loginState,
                onEmailChange = { loginState = loginState.copy(email = it, errorMessage = null) },
                onPasswordChange = { loginState = loginState.copy(password = it, errorMessage = null) },
                onSubmit = {
                    if (loginState.email.isNotBlank() && loginState.password.isNotBlank()) {
                        dashboardViewModel.loadVehicles()
                        notificationsViewModel.loadNotifications()
                        currentRoute = selectedBottomRoute
                    } else {
                        loginState = loginState.copy(errorMessage = "Completa usuario y contraseña")
                    }
                },
                onForgotPassword = { currentRoute = AppRoute.ForgotPassword },
                modifier = modifier,
            )
        }

        AppRoute.ForgotPassword -> {
            ForgotPasswordScreen(
                state = forgotState,
                onEmailChange = { forgotState = forgotState.copy(email = it) },
                onSubmit = {
                    forgotState = forgotState.copy(successMessage = "Correo enviado correctamente")
                },
                onBack = { currentRoute = AppRoute.Login },
                modifier = modifier,
            )
        }

        else -> {
            AuthenticatedAppShell(
                selectedRoute = currentRoute,
                onRouteSelected = { route ->
                    selectedBottomRoute = route
                    currentRoute = route
                },
                onLogout = {
                    loginState = LoginUiState()
                    currentRoute = AppRoute.Login
                },
                dashboardState = dashboardState,
                notificationsState = notificationsState,
                onDashboardRefresh = { dashboardViewModel.loadVehicles() },
                onNotificationsRefresh = { notificationsViewModel.loadNotifications() },
                onNotificationDelete = { notificationsViewModel.deleteNotification(it) },
                modifier = modifier,
            )
        }
    }
}
