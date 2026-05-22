package com.nivar.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.unit.sp
import android.widget.FrameLayout

// [Component] A standard 320x50 Banner Ad Container.
// Designed to wrap Google AdMob / Facebook Audience Network views.
@Composable
fun StandardBannerAd() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp) // Standard Mobile Banner Height
            .background(Color(0xFFEEEEEE)), // Light Gray Placeholder background
        contentAlignment = Alignment.Center
    ) {
        // [Integration Point] In production, replace this Text with AndroidView wrapping AdView.
        /*
        AndroidView(
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    setAdUnitId("ca-app-pub-3940256099942544/6300978111") // Test ID
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
        */
        
        // [Visual] Minimal label as requested (No dummy copy, just placement indication).
        Text("AD SPACE", color = Color.Gray, style = androidx.compose.material3.MaterialTheme.typography.labelSmall)
    }
}

// [Component] A Premium Native Ad Container.
// Designed to blend seamlessly with the content feed while maintaining disclosure.
@Composable
fun NativeAdContainer() {
    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp), // Spacious verticality
        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp), // Modern super-rounded corners
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface
        ),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // [Background] Subtle gradient or image placeholder
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(
                                androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant,
                                androidx.compose.material3.MaterialTheme.colorScheme.surface
                            )
                        )
                    )
            )
            
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .align(Alignment.Center)
            ) {
                // [Badge] "Sponsored"
                Text(
                    text = "CIVIC PARTNER",
                    style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // [Placeholder Content]
                Text(
                    text = "Premium Ad Placement",
                    style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Seamlessly integrated for a distraction-free public service experience.",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // [Action Button Placeholder]
            androidx.compose.material3.Button(
                onClick = {},
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Learn More")
            }
        }
    }
}
