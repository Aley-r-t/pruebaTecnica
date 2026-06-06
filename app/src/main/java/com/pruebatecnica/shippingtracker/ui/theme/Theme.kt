package com.pruebatecnica.shippingtracker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    secondary = Cyan,
    tertiary = Purple,
    background = ShippingWhite,
    surface = ShippingWhite,
    error = DangerRed,
    onPrimary = ShippingWhite,
    onSecondary = ShippingBlack,
    onTertiary = ShippingWhite,
    onBackground = ShippingBlack,
    onSurface = ShippingBlack,
    onError = ShippingWhite,
)

private val DarkColorScheme = darkColorScheme(
    primary = Cyan,
    secondary = PrimaryBlue,
    tertiary = Purple,
    background = ShippingBlack,
    surface = ShippingBlack,
    error = DangerRed,
    onPrimary = ShippingBlack,
    onSecondary = ShippingWhite,
    onTertiary = ShippingWhite,
    onBackground = ShippingWhite,
    onSurface = ShippingWhite,
    onError = ShippingWhite,
)

@Composable
fun ShippingTrackerTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        content = content,
    )
}
