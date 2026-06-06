package com.pruebatecnica.shippingtracker

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.pruebatecnica.shippingtracker.ui.ShippingTrackerApp
import com.pruebatecnica.shippingtracker.ui.theme.ShippingTrackerTheme
import org.junit.Rule
import org.junit.Test

class ShippingTrackerAppTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun appShell_displaysAuthenticatedBottomNavigation() {
        composeTestRule.setContent {
            ShippingTrackerTheme {
                ShippingTrackerApp()
            }
        }

        composeTestRule.onNodeWithText("Shipping Map").assertIsDisplayed()
        composeTestRule.onNodeWithText("Map").assertIsDisplayed()
        composeTestRule.onNodeWithText("Billings").assertIsDisplayed()
        composeTestRule.onNodeWithText("Profile").assertIsDisplayed()
        composeTestRule.onNodeWithText("Notifications").assertIsDisplayed()
    }
}
