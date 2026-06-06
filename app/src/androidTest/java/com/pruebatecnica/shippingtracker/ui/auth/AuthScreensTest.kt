package com.pruebatecnica.shippingtracker.ui.auth

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.pruebatecnica.shippingtracker.ui.theme.ShippingTrackerTheme
import org.junit.Rule
import org.junit.Test

class AuthScreensTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_displaysFieldsActionAndForgotNavigationText() {
        composeTestRule.setContent {
            ShippingTrackerTheme {
                LoginScreen(
                    state = LoginUiState(errorMessage = "Invalid credentials"),
                    onEmailChange = {},
                    onPasswordChange = {},
                    onSubmit = {},
                    onForgotPassword = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Password").assertIsDisplayed()
        composeTestRule.onNodeWithText("Log in").assertIsDisplayed()
        composeTestRule.onNodeWithText("Forgot password?").assertIsDisplayed()
        composeTestRule.onNodeWithText("Invalid credentials").assertIsDisplayed()
    }

    @Test
    fun forgotPasswordScreen_displaysEmailActionAndFeedbackText() {
        composeTestRule.setContent {
            ShippingTrackerTheme {
                ForgotPasswordScreen(
                    state = ForgotPasswordUiState(successMessage = "Reset sent"),
                    onEmailChange = {},
                    onSubmit = {},
                    onBack = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Recover password").assertIsDisplayed()
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Send reset link").assertIsDisplayed()
        composeTestRule.onNodeWithText("Reset sent").assertIsDisplayed()
    }
}
