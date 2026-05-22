package com.nivar.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

// [Component] Adaptive Scaffold for Foldables & Tablets.
// @deprecated This component is superseded by the NavigationRail implementation
// in MainActivity.kt. Retained for backward compatibility only.
// Uses LocalConfiguration to detect screen width (simple approach).
// In production, WindowSizeClass is preferred, but this is robust for a quick upgrade.

enum class DevicePosture {
    Compact,        // Phone
    Expanded        // Tablet / Unfolded Foldable
}

@Composable
fun AdaptiveScaffold(
    listPane: @Composable () -> Unit,
    detailPane: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    
    // [Logic] Breakpoint for Tablet/Foldable (600dp is standard)
    val posture = if (screenWidth >= 600.dp) DevicePosture.Expanded else DevicePosture.Compact
    
    Surface(color = MaterialTheme.colorScheme.background) {
        if (posture == DevicePosture.Expanded) {
            // [Layout] Two-Pane Layout for Large Screens
            Row(modifier = Modifier.fillMaxSize()) {
                // [Left Pane] Master (List) - Fixed Width or Weight
                Box(
                    modifier = Modifier
                        .width(360.dp) // Fixed standard width for master pane
                        .fillMaxHeight()
                ) {
                    listPane()
                }
                
                // [Divider] Vertical Line
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.outlineVariant)
                )
                
                // [Right Pane] Details - Fills remaining space
                Box(modifier = Modifier.weight(1f)) {
                    detailPane()
                }
            }
        } else {
            // [Layout] Single Pane for Phones
            // Defaults to listPane. Navigation handles detail transition.
            Box(modifier = Modifier.fillMaxSize()) {
                listPane()
            }
        }
    }
}
