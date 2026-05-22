package com.nivar.app.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.nivar.app.ui.theme.*

// ===== Ministry Full Profile Screen =====

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinistryDetailScreen(
    ministryId: String,
    onBack: () -> Unit
) {
    val ministry = remember { getMinistryData().find { it.id == ministryId } }

    if (ministry == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Ministry not found", style = MaterialTheme.typography.bodyLarge)
        }
        return
    }

    val context = LocalContext.current
    var selectedDeptIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // === TOP BAR ===
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = NivarNavy
                )
            }
            Text(
                "Ministry Profile",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = NivarNavy
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            // === HEADER: Ministry Name + Tags ===
            Text(
                text = ministry.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = NivarNavy
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Tag chips
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = NivarSlate100
                ) {
                    Text(
                        ministry.categoryLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = NivarSlate500,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                if (ministry.functionLabel.isNotBlank()) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = getCategoryColor(ministry.category)
                    ) {
                        Text(
                            ministry.functionLabel,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            // Address
            if (ministry.address != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = NivarSlate500,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = ministry.address,
                        style = MaterialTheme.typography.bodySmall,
                        color = NivarSlate500
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // === ACTION BAR (5 buttons) ===
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ActionBarButton(
                    icon = Icons.Default.Phone,
                    label = "Call",
                    enabled = ministry.phone != null,
                    onClick = {
                        ministry.phone?.let {
                            context.startActivity(Intent(Intent.ACTION_DIAL, "tel:$it".toUri()))
                        }
                    }
                )
                ActionBarButton(
                    icon = Icons.Default.Email,
                    label = "Email",
                    enabled = ministry.email != null,
                    onClick = {
                        ministry.email?.let {
                            context.startActivity(Intent(Intent.ACTION_SENDTO, "mailto:$it".toUri()))
                        }
                    }
                )
                ActionBarButton(
                    icon = Icons.Default.Language,
                    label = "Portal",
                    onClick = {
                        com.nivar.app.utils.LinkUtils.openUrl(context, ministry.url)
                    }
                )
                ActionBarButton(
                    icon = Icons.Default.ContentCopy,
                    label = "Copy",
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val details = buildString {
                            appendLine(ministry.name)
                            ministry.phone?.let { appendLine("Phone: $it") }
                            ministry.email?.let { appendLine("Email: $it") }
                            ministry.address?.let { appendLine("Address: $it") }
                            appendLine("Portal: ${ministry.url}")
                        }
                        clipboard.setPrimaryClip(ClipData.newPlainText("Ministry Details", details))
                        Toast.makeText(context, "Details copied!", Toast.LENGTH_SHORT).show()
                    }
                )
                ActionBarButton(
                    icon = Icons.Default.BookmarkBorder,
                    label = "Save",
                    onClick = {
                        Toast.makeText(context, "Saved to contacts!", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            // === KEY OFFICIALS ===
            if (ministry.officials.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Key Officials",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = NivarNavy
                )
                Spacer(modifier = Modifier.height(8.dp))

                ministry.officials.forEach { official ->
                    OfficialCard(official, context)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // === DEPARTMENTS (TABBED INTERFACE) ===
            if (ministry.subOrganizations.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Departments & Organizations",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = NivarNavy
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Pill tab row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ministry.subOrganizations.forEachIndexed { index, subOrg ->
                        val isSelected = selectedDeptIndex == index
                        // Shorten tab label to first word or abbreviation
                        val tabLabel = subOrg.name
                            .replace(Regex("\\(.*?\\)"), "")
                            .split(" ")
                            .take(2)
                            .joinToString(" ")
                            .trim()

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) NivarNavy else NivarSlate100,
                            modifier = Modifier.clickable { selectedDeptIndex = index }
                        ) {
                            Text(
                                text = tabLabel,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isSelected) Color.White else NivarSlate700,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                maxLines = 1
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Bento Box card for selected department
                val selectedSubOrg = ministry.subOrganizations[selectedDeptIndex]
                DepartmentBentoCard(selectedSubOrg, ministry.category, context)
            }

            // === SOCIAL CONNECT ===
            val hasSocial = ministry.facebook != null || ministry.twitter != null ||
                            ministry.instagram != null || ministry.youtube != null
            if (hasSocial) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Connect",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = NivarNavy
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ministry.facebook?.let { url ->
                        SocialButton("f", Color(0xFF1877F2), url, context)
                    }
                    ministry.twitter?.let { url ->
                        SocialButton("𝕏", Color(0xFF0F1419), url, context)
                    }
                    ministry.instagram?.let { url ->
                        SocialButton("IG", Color(0xFFE4405F), url, context)
                    }
                    ministry.youtube?.let { url ->
                        SocialButton("▶", Color(0xFFFF0000), url, context)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ===== Official Full Card =====

@Composable
private fun OfficialCard(official: Official, context: Context) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = NivarSlate50),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(NivarNavy),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = official.name.split(" ").take(2).joinToString("") { it.first().uppercase() },
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Name + Designation
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = official.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = official.designation,
                    style = MaterialTheme.typography.bodySmall,
                    color = NivarRoyal
                )
            }

            // Mini action icons
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                if (official.phone != null) {
                    IconButton(
                        onClick = {
                            context.startActivity(Intent(Intent.ACTION_DIAL, "tel:${official.phone}".toUri()))
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Phone,
                            contentDescription = "Call",
                            tint = NivarNavy,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                if (official.email != null) {
                    IconButton(
                        onClick = {
                            context.startActivity(Intent(Intent.ACTION_SENDTO, "mailto:${official.email}".toUri()))
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = "Email",
                            tint = NivarNavy,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

// ===== Department Bento Box Card =====

@Composable
private fun DepartmentBentoCard(subOrg: SubOrganization, category: String, context: Context) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Dept name
            Text(
                text = subOrg.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Tags
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Type tag
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = NivarSlate100
                ) {
                    Text(
                        // Parse type from the subOrg type or default
                        if (subOrg.type.length < 30) subOrg.type.split(",").first().trim()
                        else "Department",
                        style = MaterialTheme.typography.labelSmall,
                        color = NivarSlate500,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 10.sp
                    )
                }
                // Category accent tag
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = getCategoryColor(category).copy(alpha = 0.12f)
                ) {
                    Text(
                        getCategoryIcon(category),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Description (2 lines)
            if (subOrg.type.length >= 30) {
                Text(
                    text = subOrg.type,
                    style = MaterialTheme.typography.bodySmall,
                    color = NivarSlate700,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // URL + Visit Portal
            Text(
                text = subOrg.url,
                style = MaterialTheme.typography.labelSmall,
                color = NivarSlate300,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            TextButton(
                onClick = {
                    com.nivar.app.utils.LinkUtils.openUrl(context, subOrg.url)
                },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    "Visit Portal →",
                    style = MaterialTheme.typography.labelLarge,
                    color = NivarRoyal
                )
            }
        }
    }
}

// ===== Social Media Button =====

@Composable
private fun SocialButton(label: String, color: Color, url: String, context: Context) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(color)
            .clickable {
                com.nivar.app.utils.LinkUtils.openSocialLink(context, url)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}
