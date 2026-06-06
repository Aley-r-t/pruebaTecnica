package com.pruebatecnica.shippingtracker.ui.dashboard

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.pruebatecnica.shippingtracker.ui.theme.ShippingTrackerTheme
import org.junit.Rule
import org.junit.Test

class DashboardScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun dashboardScreen_displaysVehicleMarkerLabels() {
        composeTestRule.setContent {
            ShippingTrackerTheme {
                DashboardScreen(
                    state = DashboardUiState(
                        markers = listOf(
                            MapVehicleMarker("ABC-123", "72.5 km/h", 0.5f, 0.5f, 90.0, 0xFF23B4D9),
                        ),
                    ),
                    onRefresh = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Shipping Map").assertIsDisplayed()
        composeTestRule.onNodeWithText("ABC-123").assertIsDisplayed()
        composeTestRule.onNodeWithText("72.5 km/h").assertIsDisplayed()
    }

    @Test
    fun dashboardScreen_displaysErrorAndRefreshAction() {
        composeTestRule.setContent {
            ShippingTrackerTheme {
                DashboardScreen(
                    state = DashboardUiState(errorMessage = "Vehicles unavailable"),
                    onRefresh = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Vehicles unavailable").assertIsDisplayed()
        composeTestRule.onNodeWithText("Refresh vehicles").assertIsDisplayed()
    }
}
