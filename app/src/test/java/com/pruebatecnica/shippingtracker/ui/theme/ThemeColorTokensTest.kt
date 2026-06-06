package com.pruebatecnica.shippingtracker.ui.theme

import org.junit.Assert.assertEquals
import org.junit.Test

class ThemeColorTokensTest {
    @Test
    fun requiredPalette_matchesAssignmentHexValues() {
        assertEquals(0xFF0041BA, PrimaryBlueHex)
        assertEquals(0xFF23B4D9, CyanHex)
        assertEquals(0xFF9841D1, PurpleHex)
        assertEquals(0xFFFFFFFF, WhiteHex)
        assertEquals(0xFFF00101, DangerRedHex)
        assertEquals(0xFF000000, BlackHex)
    }
}
