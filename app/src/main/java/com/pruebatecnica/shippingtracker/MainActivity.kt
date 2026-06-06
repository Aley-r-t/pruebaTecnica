package com.pruebatecnica.shippingtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pruebatecnica.shippingtracker.ui.ShippingTrackerApp
import com.pruebatecnica.shippingtracker.ui.theme.ShippingTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShippingTrackerTheme {
                ShippingTrackerApp()
            }
        }
    }
}
