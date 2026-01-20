package in.civicos.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun PanicScreen(paddingValues: PaddingValues) {
    var statusText by remember { mutableStateOf("Press for Emergency Assistance") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Color(0xFFFFEBEE)), // Red-50
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(200.dp)
                .shadow(elevation = 10.dp, shape = CircleShape)
                .background(Color.Red, CircleShape)
                .clickable {
                    isLoading = true
                    statusText = "Locating..."
                    // Mock Sync
                    // In real app, launch coroutine to call API
                }
        ) {
             if (isLoading) {
                 CircularProgressIndicator(color = Color.White)
                 LaunchedEffect(Unit) {
                     delay(2000)
                     isLoading = false
                     statusText = "📍 Ward 12, Indiranagar\n👮 Halasuru PS (1.2km)\n🚑 Manipal Hospital (2km)\nProtocol: Golden Hour Active."
                 }
             } else {
                 Text("SOS", color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.Bold)
             }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = statusText,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
    }
}
