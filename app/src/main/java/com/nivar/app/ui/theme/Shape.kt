package com.nivar.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// ===== Nivar Design System — Expressive Shape Tokens =====
// M3 Expressive utilizes larger radii and more pronounced forms

val Shapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),      // Cards, inputs
    large = RoundedCornerShape(28.dp),       // Sheets, large modals
    extraLarge = RoundedCornerShape(32.dp)   // Floating elements
)
