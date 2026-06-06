package com.pruebatecnica.shippingtracker.ui.navigation

enum class AppRoute(
    val title: String,
    val showInBottomBar: Boolean = false,
) {
    Splash(""),
    Login(""),
    ForgotPassword(""),
    Dashboard("Monitorea tu Envío", showInBottomBar = true),
    Billings("Billings", showInBottomBar = true),
    Profile("Perfil de Usuario", showInBottomBar = true),
    Notifications("Notificaciones", showInBottomBar = true),
}

fun startRoute(): AppRoute = AppRoute.Splash

fun authenticatedBottomRoutes(): List<AppRoute> = listOf(
    AppRoute.Dashboard,
    AppRoute.Billings,
    AppRoute.Profile,
    AppRoute.Notifications,
)
