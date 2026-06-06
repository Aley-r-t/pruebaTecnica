package com.pruebatecnica.shippingtracker.ui.shell

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.pruebatecnica.shippingtracker.ui.navigation.AppRoute
import com.pruebatecnica.shippingtracker.ui.theme.ShippingTrackerTheme
import org.junit.Rule
import org.junit.Test

class AppShellScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun appShell_displaysBottomNavigationLabelsAndStaticPlaceholders() {
        composeTestRule.setContent {
            ShippingTrackerTheme {
                AuthenticatedAppShell(
                    selectedRoute = AppRoute.Billings,
                    onRouteSelected = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Map").assertIsDisplayed()
        composeTestRule.onNodeWithText("Billings").assertIsDisplayed()
        composeTestRule.onNodeWithText("Profile").assertIsDisplayed()
        composeTestRule.onNodeWithText("Notifications").assertIsDisplayed()
        composeTestRule.onNodeWithText("Billings summary").assertIsDisplayed()
    }
}
