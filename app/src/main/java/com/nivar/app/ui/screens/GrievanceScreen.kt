package com.nivar.app.ui.screens

import android.Manifest
import android.net.Uri
import androidx.core.net.toUri
import com.nivar.app.R
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nivar.app.ui.components.CameraCapture
import com.nivar.app.ui.viewmodel.GrievanceUiState
import com.nivar.app.ui.viewmodel.NivarViewModel

data class GrievanceCategory(
    val id: String,
    val name: String,
    val icon: ImageVector,
    val color: Color
)

// Step indicator for grievance flow
@Composable
fun GrievanceStepIndicator(currentStep: Int) {
    val steps = listOf("Category", "Details", "Evidence", "Review")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, label ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = if (index <= currentStep) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.outlineVariant,
                    modifier = Modifier.size(28.dp)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text(
                            "${index + 1}",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (index <= currentStep) MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    label,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (index <= currentStep) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (index < steps.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                    thickness = 2.dp,
                    color = if (index < currentStep) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    }
}

@Composable
fun GrievanceScreen(
    viewModel: NivarViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf<GrievanceCategory?>(null) }

    val categories = listOf(
        GrievanceCategory("law_violent", "Major Crime", Icons.Filled.Warning, Color(0xFFB71C1C)),
        GrievanceCategory("law_civil", "Police & Theft", Icons.Filled.Security, Color(0xFFD32F2F)),
        GrievanceCategory("cyber", "Cyber Crime", Icons.Filled.PhonelinkLock, Color(0xFF263238)),
        GrievanceCategory("defense", "Natl. Security", Icons.Filled.Shield, Color(0xFF1B5E20)),
        GrievanceCategory("customs", "Smuggling", Icons.Filled.Luggage, Color(0xFF37474F)),
        GrievanceCategory("corruption", "Corruption", Icons.Filled.MoneyOff, Color(0xFF5D4037)),
        GrievanceCategory("infra", "Infrastructure", Icons.Filled.Build, Color(0xFF1C4D8D)),
        GrievanceCategory("sanitation", "Sanitation", Icons.Filled.Delete, Color(0xFF43A047)),
        GrievanceCategory("env", "Environment", Icons.Filled.Nature, Color(0xFF388E3C)),
        GrievanceCategory("banking", "Banking", Icons.Filled.AccountBalance, Color(0xFF0D47A1)),
        GrievanceCategory("commerce", "Commerce", Icons.Filled.Business, Color(0xFF006064)),
        GrievanceCategory("tax", "Income Tax", Icons.Filled.Receipt, Color(0xFF455A64)),
        GrievanceCategory("consumer", "Consumer Rights", Icons.Filled.ShoppingCart, Color(0xFF8E24AA)),
        GrievanceCategory("ip", "Intellectual Property", Icons.Filled.Lock, Color(0xFF6A1B9A)),
        GrievanceCategory("railways", "Railways", Icons.Filled.DirectionsTransit, Color(0xFFBF360C)),
        GrievanceCategory("traffic", "Traffic", Icons.Filled.DirectionsCar, Color(0xFFF57C00)),
        GrievanceCategory("ports", "Ports", Icons.Filled.DirectionsBoat, Color(0xFF0277BD)),
        GrievanceCategory("health", "Healthcare", Icons.Filled.LocalHospital, Color(0xFFE91E63)),
        GrievanceCategory("education", "Education", Icons.Filled.School, Color(0xFFF9A825)),
        GrievanceCategory("agri", "Agriculture", Icons.Filled.Grass, Color(0xFF2E7D32)),
        GrievanceCategory("telecom", "Telecom", Icons.Filled.Phone, Color(0xFF00838F))
    )

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isAnonymous by remember { mutableStateOf(false) }
    var selectedUrgency by remember { mutableStateOf("Medium") }
    val urgencyLevels = listOf("Low", "Medium", "High", "Critical")
    
    val uiState by viewModel.grievanceState.collectAsState()
    var showCamera by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val currentStep = if (selectedCategory == null) 0 else if (imageUri == null) 1 else 2
    
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted -> if (isGranted) showCamera = true }

    if (showCamera) {
        CameraCapture(
            onImageCaptured = { imageUri = it; showCamera = false },
            onError = { showCamera = false }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            // Step indicator
            GrievanceStepIndicator(currentStep)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (selectedCategory != null) {
                    IconButton(onClick = { selectedCategory = null; imageUri = null }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
                    }
                }
                Text(
                    text = selectedCategory?.name ?: "Select Issue Category",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            if (selectedCategory == null) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(categories.size) { index ->
                        val cat = categories[index]
                        Card(
                            modifier = Modifier
                                .height(130.dp)
                                .clickable { selectedCategory = cat },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize().padding(8.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(cat.icon, contentDescription = null, tint = cat.color, modifier = Modifier.size(40.dp))
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    cat.name, 
                                    style = MaterialTheme.typography.titleSmall, 
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 2,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                            }
                        }
                    }
                }
            } else {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = selectedCategory!!.color.copy(alpha = 0.1f)),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp)) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = selectedCategory!!.color)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = if (selectedCategory!!.id == "corruption") 
                                    "Reports of corruption are encrypted. You can choose to remain anonymous." 
                                    else "Reporting infrastructure issues helps us prioritize repairs.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = description,
                        onValueChange = { if (it.length <= 1000) description = it },
                        label = { Text("Detailed Description") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 5,
                        shape = RoundedCornerShape(12.dp),
                        supportingText = {
                            Text(
                                "${description.length}/1000",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (description.length > 900) MaterialTheme.colorScheme.error
                                        else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Urgency Selector
                    Text("Urgency", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        urgencyLevels.forEach { level ->
                            val isSelected = selectedUrgency == level
                            val urgencyColor = when (level) {
                                "Low" -> Color(0xFF16A34A)
                                "Medium" -> Color(0xFFF59E0B)
                                "High" -> Color(0xFFEA580C)
                                "Critical" -> Color(0xFFDC2626)
                                else -> MaterialTheme.colorScheme.primary
                            }
                            Surface(
                                onClick = { selectedUrgency = level },
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) urgencyColor else MaterialTheme.colorScheme.surface,
                                border = BorderStroke(1.dp, if (isSelected) urgencyColor else MaterialTheme.colorScheme.outlineVariant),
                                modifier = Modifier.weight(1f).height(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                    Text(
                                        level,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text("Evidence", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .height(160.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                            .border(BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant), RoundedCornerShape(12.dp)) // Fallback if dash not easy
                            .clickable { permissionLauncher.launch(Manifest.permission.CAMERA) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (imageUri != null) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(48.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Photo Attached", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Filled.Add, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Tap to capture photo", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))

                    if (selectedCategory!!.id in listOf("corruption", "tax", "law_violent", "law_civil", "customs", "defense", "cyber")) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                                .padding(8.dp)
                        ) {
                            Checkbox(checked = isAnonymous, onCheckedChange = { isAnonymous = it })
                            Text("Submit Anonymously", style = MaterialTheme.typography.bodyLarge)
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { viewModel.submitGrievance(title, description, 0.0, 0.0, selectedCategory!!.id, imageUri?.toString(), isAnonymous) },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = selectedCategory!!.color),
                        shape = RoundedCornerShape(12.dp),
                        enabled = title.isNotEmpty() && uiState !is GrievanceUiState.Loading
                    ) {
                         if (uiState is GrievanceUiState.Loading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Submit Report", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                    
                    if (uiState is GrievanceUiState.Success) {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text("Report Submitted Successfully", color = Color(0xFF2E7D32), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    "Public pressure forces faster action. Share this on X (Twitter) to notify the authorities immediately.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF1B5E20)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                                    shape = RoundedCornerShape(8.dp),
                                    onClick = {
                                        val tweet = "@MyGovIndia @PMOIndia I reported a ${selectedCategory!!.name} issue via @NivarApp. Urgent attention requested! #CivicDuty"
                                        val url = "https://twitter.com/intent/tweet?text=${Uri.encode(tweet)}"
                                        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                                        try {
                                            context.startActivity(intent)
                                        } catch (e: Exception) { }
                                    }
                                ) {
                                    Text("Amplify on X", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedButton(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp),
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                                            data = "mailto:".toUri()
                                            putExtra(Intent.EXTRA_EMAIL, arrayOf("commissioner@bbmp.gov.in"))
                                            putExtra(Intent.EXTRA_CC, arrayOf("admin@nivar.in"))
                                            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.grievance_email_subject, title, selectedCategory!!.name))
                                            putExtra(Intent.EXTRA_TEXT, "Using Nivar App:\n\n$description\n\nLocation: 0.0, 0.0\n\nPlease resolve immediately.")
                                        }
                                        try {
                                            context.startActivity(intent)
                                        } catch (e: Exception) { }
                                    }
                                ) {
                                    Icon(Icons.Filled.Info, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Send Personal Email Copy")
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}
