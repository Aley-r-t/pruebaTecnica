package com.pruebatecnica.shippingtracker.ui.shell

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.pruebatecnica.shippingtracker.ui.theme.ShippingBlack
import com.pruebatecnica.shippingtracker.ui.theme.ShippingWhite

@Composable
fun BillingsScreen(modifier: Modifier = Modifier) {
    CenteredLabelScreen(label = "BILLINGS", modifier = modifier)
}

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    CenteredLabelScreen(label = "PERFIL", modifier = modifier)
}

@Composable
private fun CenteredLabelScreen(label: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize().background(ShippingWhite),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = ShippingBlack,
        )
    }
}
