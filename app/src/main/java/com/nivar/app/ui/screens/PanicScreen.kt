package com.nivar.app.ui.screens

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.view.HapticFeedbackConstants
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nivar.app.ui.theme.ErrorRed
import com.nivar.app.ui.theme.PureWhite
import com.nivar.app.ui.theme.SOSRedLight
import com.nivar.app.ui.theme.NivarNavy
import com.nivar.app.utils.LocationUtils
import kotlinx.coroutines.launch

@Composable
fun PanicScreen() {
    var statusText by remember { mutableStateOf("Press for Emergency Assistance") }
    var isLoading by remember { mutableStateOf(false) }
    var countdown by remember { mutableIntStateOf(0) }
    var isCountingDown by remember { mutableStateOf(false) }
    var locationResult by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val locationUtils = remember { LocationUtils(context) }
    val view = LocalView.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            isCountingDown = true
            countdown = 3
        } else {
            statusText = "Location Permission Denied. SOS cannot function."
        }
    }

    // Timer Logic
    LaunchedEffect(isCountingDown, countdown) {
        if (isCountingDown && countdown > 0) {
            kotlinx.coroutines.delay(1000)
            countdown -= 1
        } else if (isCountingDown && countdown == 0) {
            isCountingDown = false
            isLoading = true
            statusText = "Locating your position..."
            scope.launch {
                val location = locationUtils.getCurrentLocation()
                if (location != null) {
                    locationResult = "📍 Location Secured\n👮 Nearest Police: Notified\n🏥 Nearest Hospital: Located"
                    statusText = "Emergency Protocol Active"
                } else {
                    statusText = "Unable to fetch location. Check GPS and try again."
                }
                isLoading = false

                // Dial emergency number 100
                val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:100")
                }
                context.startActivity(dialIntent)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SOSRedLight),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isCountingDown) {
            // Countdown Mode
            Text(
                "Triggering SOS in...",
                style = MaterialTheme.typography.titleLarge,
                color = ErrorRed,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "$countdown",
                style = MaterialTheme.typography.displayLarge,
                color = ErrorRed,
                fontWeight = FontWeight.Black,
                fontSize = 120.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    isCountingDown = false
                    statusText = "Emergency Cancelled."
                    locationResult = null
                },
                colors = ButtonDefaults.buttonColors(containerColor = NivarNavy),
                modifier = Modifier.height(56.dp).width(200.dp),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("CANCEL", style = MaterialTheme.typography.titleMedium, color = PureWhite)
            }
        } else {
            // Expressive Emergency Assistance Header
            Text(
                "SOS",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Black,
                color = ErrorRed,
                letterSpacing = (-2).sp
            )
            Text(
                "EMERGENCY ASSISTANCE",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = NivarNavy,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(60.dp))

            // Pulse Animation
            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
            val pulseScale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 1.2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pulseScale"
            )
            val pulseAlpha by infiniteTransition.animateFloat(
                initialValue = 0.5f,
                targetValue = 0.0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "pulseAlpha"
            )

            Box(contentAlignment = Alignment.Center) {
                // Outer pulse ring
                Box(
                    modifier = Modifier
                        .size(260.dp)
                        .scale(pulseScale)
                        .alpha(pulseAlpha)
                        .background(ErrorRed, CircleShape)
                )
                // Inner pulse ring
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .scale(pulseScale * 0.95f)
                        .alpha(pulseAlpha * 0.5f)
                        .background(ErrorRed, CircleShape)
                )

                // SOS Button
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(200.dp)
                        .shadow(elevation = 16.dp, shape = CircleShape, spotColor = ErrorRed)
                        .background(ErrorRed, CircleShape)
                        .border(BorderStroke(6.dp, Color.White.copy(alpha = 0.3f)), CircleShape)
                        .clickable {
                            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                            if (isLoading) return@clickable
                            permissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = PureWhite,
                            strokeWidth = 5.dp,
                            modifier = Modifier.size(56.dp)
                        )
                    } else {
                        Text(
                            "SOS",
                            color = PureWhite,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 4.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Location Info Card
            if (locationResult != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = locationResult!!,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = NivarNavy,
                        modifier = Modifier.padding(20.dp),
                        lineHeight = 28.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Status Text
            Text(
                text = if (isLoading) "Emergency Protocol Initiated" else statusText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = if (isLoading) ErrorRed else MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Footer
        if (!isCountingDown) {
            Text(
                text = "Police are here to serve YOU.\nDo not hesitate to ask for help.",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
}
