package com.pruebatecnica.shippingtracker.ui.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AppShellNavigationTest {
    @Test
    fun authenticatedBottomRoutes_includeDashboardBillingsProfileAndNotifications() {
        val routes = authenticatedBottomRoutes()

        assertEquals(listOf(AppRoute.Dashboard, AppRoute.Billings, AppRoute.Profile, AppRoute.Notifications), routes)
        assertTrue(routes.all { route -> route.showInBottomBar })
    }

    @Test
    fun navigationLabels_matchVisibleBottomBarText() {
        assertEquals("Monitorea tu Envío", AppRoute.Dashboard.title)
        assertEquals("Billings", AppRoute.Billings.title)
        assertEquals("Perfil de Usuario", AppRoute.Profile.title)
        assertEquals("Notificaciones", AppRoute.Notifications.title)
    }
}
