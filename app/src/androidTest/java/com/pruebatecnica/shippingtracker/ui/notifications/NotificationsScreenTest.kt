package com.pruebatecnica.shippingtracker.ui.notifications

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.pruebatecnica.shippingtracker.domain.NotificationItem
import com.pruebatecnica.shippingtracker.ui.theme.ShippingTrackerTheme
import org.junit.Rule
import org.junit.Test

class NotificationsScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun notificationsScreen_displaysInvoiceStatusCreatedDateAndDeleteAction() {
        composeTestRule.setContent {
            ShippingTrackerTheme {
                NotificationsScreen(
                    state = NotificationsUiState(
                        notifications = listOf(NotificationItem(1, "INV-001", "pending", "2026-06-05")),
                    ),
                    onRefresh = {},
                    onDelete = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Notifications").assertIsDisplayed()
        composeTestRule.onNodeWithText("INV-001").assertIsDisplayed()
        composeTestRule.onNodeWithText("pending").assertIsDisplayed()
        composeTestRule.onNodeWithText("2026-06-05").assertIsDisplayed()
        composeTestRule.onNodeWithText("Delete").assertIsDisplayed()
    }
}
