package com.pruebatecnica.shippingtracker.ui.navigation

import org.junit.Assert.assertEquals
import org.junit.Test

class AppRouteTest {
    @Test
    fun startRoute_isSplashShell() {
        assertEquals(AppRoute.Splash, startRoute())
        assertEquals("", startRoute().title)
    }
}
