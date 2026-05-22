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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.nivar.app.ui.theme.*
import kotlinx.coroutines.launch

// ===== Data Models =====

data class SubOrganization(
    val name: String,
    val url: String,
    val type: String // "Department", "Commission", "Committee", "Autonomous Body", etc.
)

data class Official(
    val name: String,
    val designation: String,
    val email: String? = null,
    val phone: String? = null
)

data class MinistryContact(
    val id: String,
    val name: String,
    val url: String,
    val educationTip: String,
    val phone: String? = null,
    val email: String? = null,
    val facebook: String? = null,
    val twitter: String? = null,
    val instagram: String? = null,
    val youtube: String? = null,
    val address: String? = null,
    val officials: List<Official> = emptyList(),
    val subOrganizations: List<SubOrganization> = emptyList(),
    // New fields for Bento Box redesign
    val category: String = "Other",         // Defence, Health, Education, Finance, Law, Agriculture, Other
    val categoryLabel: String = "Central Ministry",
    val functionLabel: String = ""           // e.g. "Agriculture & Food", "Chemicals & Pharma"
)

// ===== Category Helpers =====

fun getCategoryColor(category: String): Color = when (category) {
    "Executive" -> NivarNavy
    "Defence" -> CategoryDefence
    "Health" -> CategoryHealth
    "Education" -> CategoryEducation
    "Finance" -> CategoryFinance
    "Law" -> CategoryLaw
    "Agriculture" -> CategoryAgri
    else -> CategoryDefault
}

fun getCategoryIcon(category: String): String = when (category) {
    "Executive" -> "🇮🇳"
    "Defence" -> "🛡️"
    "Health" -> "⚕️"
    "Education" -> "🎓"
    "Finance" -> "💰"
    "Law" -> "⚖️"
    "Agriculture" -> "🌾"
    else -> "🏛️"
}

// ===== Directory Screen =====

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectoryScreen(onMinistryClick: (String) -> Unit = {}) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Executive", "Defence", "Law", "Health", "Education", "Finance", "Other")

    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var ministries by remember { mutableStateOf(getMinistryData()) }

    val filteredMinistries = ministries.filter { ministry ->
        val matchesSearch = searchQuery.isBlank() ||
            ministry.name.contains(searchQuery, ignoreCase = true) ||
            ministry.educationTip.contains(searchQuery, ignoreCase = true)
        val matchesFilter = selectedFilter == "All" || 
            (selectedFilter == "Executive" && ministry.category == "Executive") ||
            ministry.category == selectedFilter
        matchesSearch && matchesFilter
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Expressive Header
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Official Directory",
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Black,
                            color = NivarNavy,
                            letterSpacing = (-1).sp
                        )
                        Text(
                            text = "Connect with your Government",
                            style = MaterialTheme.typography.titleMedium,
                            color = NivarSky,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    // [SCRAPER API] Bridge Implementation
                    IconButton(
                        onClick = {
                            scope.launch {
                                isRefreshing = true
                                ministries = com.nivar.app.data.remote.ScraperApi.fetchAllMinistries()
                                kotlinx.coroutines.delay(1000)
                                isRefreshing = false
                            }
                        },
                        modifier = Modifier
                            .background(NivarIce.copy(alpha = 0.5f), CircleShape)
                            .size(48.dp)
                    ) {
                        if (isRefreshing) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                        } else {
                            Icon(Icons.Default.Refresh, contentDescription = "Scrape Latest", tint = NivarNavy)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Search bar - More Expressive
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text("Search ministries, officials...", style = MaterialTheme.typography.bodyLarge)
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, null, tint = MaterialTheme.colorScheme.primary)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Filter chips - More Spacing
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    filters.forEach { filter ->
                        val isSelected = selectedFilter == filter
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedFilter = filter },
                            label = {
                                Text(filter, style = MaterialTheme.typography.labelLarge)
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            border = null,
                            modifier = Modifier.height(40.dp)
                        )
                    }
                }
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(filteredMinistries) { ministry ->
                MinistryCard(
                    ministry = ministry,
                    onViewProfile = { onMinistryClick(ministry.id) }
                )
            }
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

// ===== Bento Box Ministry Summary Card =====

@Composable
fun MinistryCard(ministry: MinistryContact, onViewProfile: () -> Unit = {}) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(24.dp), // More Expressive Radius
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            // === TOP: Name + Category Icon ===
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = ministry.name,
                        style = MaterialTheme.typography.titleLarge, // Larger Title
                        fontWeight = FontWeight.ExtraBold,
                        color = NivarNavy,
                        lineHeight = 28.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = NivarSlate100
                        ) {
                            Text(
                                ministry.categoryLabel,
                                style = MaterialTheme.typography.labelSmall,
                                color = NivarSlate500,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        if (ministry.functionLabel.isNotBlank()) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = getCategoryColor(ministry.category).copy(alpha = 0.9f)
                            ) {
                                Text(
                                    ministry.functionLabel,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
                
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = NivarSlate50,
                    modifier = Modifier.size(52.dp)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = getCategoryIcon(ministry.category),
                            fontSize = 28.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // === HORIZONTAL ACTION BAR - Improved Styling ===
            Surface(
                color = NivarSlate50,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
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
                    VerticalDivider(modifier = Modifier.height(24.dp), color = NivarSlate200)
                    ActionBarButton(
                        icon = Icons.Default.Email,
                        label = "Email",
                        enabled = ministry.email != null,
                        onClick = {
                            ministry.email?.let {
                                val intent = Intent(Intent.ACTION_SENDTO, "mailto:$it".toUri())
                                try { context.startActivity(intent) } catch(e: Exception) {}
                            }
                        }
                    )
                    VerticalDivider(modifier = Modifier.height(24.dp), color = NivarSlate200)
                    ActionBarButton(
                        icon = Icons.Default.Language,
                        label = "Portal",
                        onClick = {
                            com.nivar.app.utils.LinkUtils.openUrl(context, ministry.url)
                        }
                    )
                }
            }

            // === KEY OFFICIALS CAROUSEL ===
            if (ministry.officials.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Key Representatives",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = NivarSlate500,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(ministry.officials) { official ->
                        OfficialChip(official)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // === BOTTOM: Education tip ===
            Surface(
                color = NivarIce.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("💡", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = ministry.educationTip,
                        style = MaterialTheme.typography.bodySmall,
                        color = NivarNavy,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                FilledTonalButton(
                    onClick = onViewProfile,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = NivarNavy,
                        contentColor = Color.White
                    )
                ) {
                    Text("View Full Profile")
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.ChevronRight, null, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

// ===== Action Bar Button =====

@Composable
fun ActionBarButton(
    icon: ImageVector,
    label: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(enabled = enabled) { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(if (enabled) NivarIce else NivarSlate100),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = if (enabled) NivarNavy else NivarSlate300,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (enabled) NivarSlate700 else NivarSlate300
        )
    }
}

// ===== Official Profile Chip =====

@Composable
fun OfficialChip(official: Official) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = NivarSlate50,
        modifier = Modifier.width(150.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar circle with initials
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(NivarNavy),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = official.name.split(" ").take(2).joinToString("") { it.first().uppercase() },
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = official.name.split(" ").last(),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = official.designation,
                    style = MaterialTheme.typography.labelSmall,
                    color = NivarSlate500,
                    fontSize = 10.sp,
                    maxLines = 1
                )
            }
        }
    }
}

// ===== Ministry Data =====

fun getMinistryData(): List<MinistryContact> = listOf(
    MinistryContact(
        id = "pmo",
        name = "Prime Minister's Office (PMO)",
        url = "https://www.pmindia.gov.in",
        educationTip = "The PMO provides secretarial assistance to the Prime Minister.",
        phone = "011-23012312",
        address = "South Block, Raisina Hill, New Delhi-110011",
        twitter = "https://x.com/PMOIndia",
        facebook = "https://www.facebook.com/PMOIndia",
        youtube = "https://www.youtube.com/@pmoindia",
        officials = listOf(
            Official("Shri Narendra Modi", "Prime Minister"),
            Official("Shri P.K. Mishra", "Principal Secretary")
        ),
        category = "Executive",
        categoryLabel = "Core Executive",
        functionLabel = "National Leadership"
    ),
    MinistryContact(
        id = "atomic",
        name = "Department of Atomic Energy (DAE)",
        url = "https://dae.gov.in",
        educationTip = "Directly under PM; focuses on nuclear power and research.",
        phone = "022-22026816",
        email = "chmn-office@dae.gov.in",
        address = "Anushakti Bhavan, CSM Marg, Mumbai - 400 001",
        category = "Executive",
        categoryLabel = "Strategic Department",
        functionLabel = "Nuclear Energy"
    ),
    MinistryContact(
        id = "space",
        name = "Department of Space (DoS / ISRO)",
        url = "https://www.isro.gov.in",
        educationTip = "Execution of space research and satellite launches.",
        phone = "080-23415474",
        address = "Antariksh Bhavan, New BEL Road, Bengaluru - 560 094",
        twitter = "https://x.com/isro",
        facebook = "https://www.facebook.com/ISRO",
        youtube = "https://www.youtube.com/@isroofficial",
        officials = listOf(
            Official("Shri S. Somanath", "Chairman ISRO / Secretary DoS")
        ),
        category = "Executive",
        categoryLabel = "Strategic Department",
        functionLabel = "Space Exploration"
    ),
    MinistryContact(
        id = "personnel_core",
        name = "Ministry of Personnel, Public Grievances & Pensions",
        url = "https://persmin.gov.in",
        educationTip = "Directly reporting to PM. Handles IAS, IPS, and public grievances.",
        phone = "011-23094848",
        email = "secy-arpg@nic.in",
        address = "North Block, New Delhi-110001",
        officials = listOf(
            Official("Shri Narendra Modi", "Minister-in-Charge"),
            Official("Shri Jitendra Singh", "Minister of State")
        ),
        category = "Executive",
        categoryLabel = "Core Executive",
        functionLabel = "Personnel & Governance"
    ),
    MinistryContact(
        id = "agri",
        name = "Ministry of Agriculture & Farmers Welfare",
        url = "https://agricoop.nic.in",
        educationTip = "PM-KISAN, Kisan Call Centre: 1800-180-1551",
        phone = "011-23382651",
        email = "agri-secy@nic.in",
        address = "Krishi Bhawan, Dr. Rajendra Prasad Road, New Delhi-110001",
        twitter = "https://x.com/AgriGoI",
        facebook = "https://www.facebook.com/AgricultureGoI",
        officials = listOf(
            Official("Shri Shivraj Singh Chouhan", "Minister", email = "agri.minister@gov.in"),
            Official("Shri Ramnath Thakur", "Minister of State", email = "mos.agri@gov.in")
        ),
        subOrganizations = listOf(
            SubOrganization("Department of Agriculture & Farmers Welfare", "https://agricoop.nic.in", "PM-KISAN, crop insurance, MSP"),
            SubOrganization("Department of Agricultural Research & Education (DARE)", "https://dare.nic.in", "ICAR, agricultural research"),
            SubOrganization("National Bank for Agriculture and Rural Development (NABARD)", "https://www.nabard.org", "Agricultural credit and rural banking"),
            SubOrganization("Food Corporation of India (FCI)", "https://fci.gov.in", "Procurement and food grain storage")
        ),
        category = "Agriculture",
        functionLabel = "Agriculture & Food"
    ),
    MinistryContact("aviation", "Ministry of Civil Aviation", "https://civilaviation.gov.in", "Flight/Airport grievances.", phone = "24610358", email = "webmaster-moca@gov.in",
        category = "Other", functionLabel = "Aviation & Transport"),
    MinistryContact(
        "ayush",
        "Ministry of AYUSH (MoA)",
        "https://ayush.gov.in",
        "Promoting Yoga and Ayurveda.",
        facebook = "https://www.facebook.com/moayush",
        twitter = "https://x.com/moayush",
        instagram = "https://www.instagram.com/ministryofayush/",
        youtube = "https://www.youtube.com/@MinistryofAYUSHofficial",
        subOrganizations = listOf(
            SubOrganization("Ayush Admissions Central Counseling Committee (AACCC)", "https://intraaaccc.gov.in/", "Committee"),
            SubOrganization("National Commission for Homoeopathy (NCH)", "https://nch.org.in/", "Commission"),
            SubOrganization("National Commission for Indian System of Medicine (NCISM)", "https://ncismindia.org/", "Commission"),
            SubOrganization("Pharmacopoeia Commission for Indian Medicine and Homoeopathy (PCIMH)", "https://pcimh.gov.in/", "Commission")
        ),
        category = "Health",
        functionLabel = "Traditional Medicine"
    ),
    MinistryContact(
        id = "chem",
        name = "Ministry of Chemicals & Fertilizers (MoCF)",
        url = "https://mocf.gov.in",
        educationTip = "Subsidized fertilizers; Pharma policies.",
        phone = "011-23070712",
        email = "min-cnf@gov.in",
        address = "Room No. 230 A, A-wing, 2nd floor, Shastri Bhawan, New Delhi-110001",
        facebook = "https://www.facebook.com/MinisterMoCF/",
        instagram = "https://www.instagram.com/ministryofchemistry/",
        officials = listOf(
            Official("Shri Jagat Prakash Nadda", "Minister", email = "min-cnf@gov.in", phone = "23383559"),
            Official("Smt. Anupriya Patel", "Minister of State", email = "mos-mocf@nic.in", phone = "23381768")
        ),
        subOrganizations = listOf(
            SubOrganization("Department of Chemicals & Petro-Chemicals (DoCP)", "https://chemicals.gov.in/", "Promotes chemical and petrochemical industries"),
            SubOrganization("Department of Fertilizers (DoFz)", "https://fert.gov.in/", "Ensures affordable fertilizers for farmers"),
            SubOrganization("Department of Pharmaceuticals (DoPs)", "https://pharma-dept.gov.in/", "Jan Aushadhi scheme; Price control")
        ),
        category = "Other",
        functionLabel = "Chemicals & Pharma"
    ),
    MinistryContact("coal", "Ministry of Coal", "https://coal.nic.in", "Coal allocation transparency.", phone = "23070522", email = "secy.moc@nic.in",
        category = "Other", functionLabel = "Energy & Mining"),
    MinistryContact("commerce", "Ministry of Commerce", "https://commerce.gov.in", "Export/Import guidelines.", phone = "23039110", email = "piyush.goyal@gov.in",
        category = "Finance", functionLabel = "Trade & Commerce"),
    MinistryContact("comm", "Ministry of Communications", "https://dot.gov.in", "Telecom services & Post.", phone = "23739191", email = "moc-office@gov.in",
        category = "Other", functionLabel = "Telecom & Postal"),
    MinistryContact(
        id = "consumer",
        name = "Ministry of Consumer Affairs, Food & Public Distribution",
        url = "https://consumeraffairs.nic.in",
        educationTip = "Consumer complaint helpline: 1915, PDS ration",
        phone = "011-23070532",
        email = "secy-consumer@nic.in",
        address = "Krishi Bhawan, Dr. Rajendra Prasad Road, New Delhi-110001",
        twitter = "https://x.com/MinOfCAFPD",
        facebook = "https://www.facebook.com/consumeraffairsministryindia",
        officials = listOf(
            Official("Shri Pralhad Joshi", "Minister", email = "consumer.minister@gov.in"),
            Official("Shri B L Verma", "Minister of State", email = "mos.consumer@gov.in")
        ),
        subOrganizations = listOf(
            SubOrganization("Department of Consumer Affairs", "https://consumeraffairs.nic.in", "Consumer protection, weights & measures"),
            SubOrganization("Department of Food & Public Distribution", "https://dfpd.gov.in", "PDS ration system, food security"),
            SubOrganization("National Consumer Helpline (NCH)", "https://consumerhelpline.gov.in", "1915 complaint registration"),
            SubOrganization("Bureau of Indian Standards (BIS)", "https://www.bis.gov.in", "Quality standards and ISI marks")
        ),
        category = "Other",
        functionLabel = "Consumer & Food"
    ),
    MinistryContact(
        id = "defence",
        name = "Ministry of Defence (MoD)",
        url = "https://www.mod.gov.in",
        educationTip = "Armed forces, ECHS, defence recruitment",
        phone = "011-23010104",
        email = "mod@nic.in",
        address = "South Block, Central Secretariat, New Delhi-110011",
        twitter = "https://x.com/DefenceMinIndia",
        facebook = "https://www.facebook.com/IndianDefenceMinistry",
        officials = listOf(
            Official("Shri Rajnath Singh", "Minister", email = "raksha.mantri@gov.in"),
            Official("Shri Sanjay Seth", "Minister of State", email = "mos.defence@gov.in")
        ),
        subOrganizations = listOf(
            SubOrganization("Indian Army", "https://indianarmy.nic.in", "Land forces recruitment and operations"),
            SubOrganization("Indian Navy", "https://indiannavy.nic.in", "Maritime operations and naval recruitment"),
            SubOrganization("Indian Air Force", "https://indianairforce.nic.in", "Aerospace operations and recruitment"),
            SubOrganization("Ex-Servicemen Contributory Health Scheme (ECHS)", "https://echs.gov.in", "Healthcare for veterans")
        ),
        category = "Defence",
        functionLabel = "National Security"
    ),
    MinistryContact(
        id = "education",
        name = "Ministry of Education",
        url = "https://education.gov.in",
        educationTip = "Anti-Ragging: 1800-180-5522, Scholarship helpline",
        phone = "011-23382698",
        email = "shiksha.mantri@gov.in",
        address = "Shastri Bhawan, Dr. Rajendra Prasad Road, New Delhi-110001",
        twitter = "https://x.com/EduMinOfIndia",
        facebook = "https://www.facebook.com/MinistryofEducationIndia",
        officials = listOf(
            Official("Shri Dharmendra Pradhan", "Minister", email = "edu.minister@gov.in"),
            Official("Smt. Annpurna Devi", "Minister of State", email = "mos.edu@gov.in")
        ),
        subOrganizations = listOf(
            SubOrganization("Department of School Education & Literacy", "https://education.gov.in/school-education", "School education, Samagra Shiksha"),
            SubOrganization("Department of Higher Education", "https://education.gov.in/higher-education", "Universities, IITs, NITs, colleges"),
            SubOrganization("University Grants Commission (UGC)", "https://www.ugc.gov.in", "Coordination of university education"),
            SubOrganization("All India Council for Technical Education (AICTE)", "https://www.aicte-india.org", "Technical education regulation")
        ),
        category = "Education",
        functionLabel = "Schools & Universities"
    ),
    MinistryContact("env", "Ministry of Environment (MoEFCC)", "https://moef.gov.in", "Report Pollution.",
        category = "Other", functionLabel = "Environment"),
    MinistryContact(
        id = "external",
        name = "Ministry of External Affairs (MEA)",
        url = "https://www.mea.gov.in",
        educationTip = "Passport Seva, consular services, visa",
        phone = "011-23016122",
        email = "fsdiv2@mea.gov.in",
        address = "South Block, Central Secretariat, New Delhi-110011",
        twitter = "https://x.com/MEAIndia",
        facebook = "https://www.facebook.com/MEAIndia",
        officials = listOf(
            Official("Dr. S Jaishankar", "Minister", email = "eam@mea.gov.in"),
            Official("Shri Pabitra Margherita", "Minister of State", email = "mos.mea@gov.in")
        ),
        subOrganizations = listOf(
            SubOrganization("Passport Seva", "https://www.passportindia.gov.in", "Online passport application and tracking"),
            SubOrganization("Consular Services", "https://www.mea.gov.in/consular.htm", "Indian missions abroad, visa services"),
            SubOrganization("Indian Council for Cultural Relations (ICCR)", "https://www.iccr.gov.in", "Cultural diplomacy and scholarships"),
            SubOrganization("Ministry of Overseas Indian Affairs", "https://moia.gov.in", "Services for NRIs and PIOs")
        ),
        category = "Other",
        functionLabel = "Foreign Affairs"
    ),
    MinistryContact(
        id = "finance",
        name = "Ministry of Finance",
        url = "https://www.finmin.nic.in",
        educationTip = "Income tax, GST, budget, banking",
        phone = "011-23092453",
        email = "fm.finance@gov.in",
        address = "North Block, Central Secretariat, New Delhi-110001",
        twitter = "https://x.com/FinMinIndia",
        facebook = "https://www.facebook.com/FinMinIndia",
        officials = listOf(
            Official("Smt. Nirmala Sitharaman", "Minister", email = "finance.minister@gov.in"),
            Official("Shri Pankaj Chaudhary", "Minister of State", email = "mos.finance@gov.in")
        ),
        subOrganizations = listOf(
            SubOrganization("Department of Revenue", "https://dor.gov.in", "Income Tax, GST, customs, CBDT, CBIC"),
            SubOrganization("Department of Economic Affairs", "https://dea.gov.in", "Budget, economy, PPP, infrastructure"),
            SubOrganization("Department of Financial Services", "https://financialservices.gov.in", "Banking, insurance, pension PFRDA"),
            SubOrganization("Reserve Bank of India (RBI)", "https://www.rbi.org.in", "Central banking, monetary policy")
        ),
        category = "Finance",
        functionLabel = "Budget & Banking"
    ),
    MinistryContact(
        id = "health",
        name = "Ministry of Health & Family Welfare (MoHFW)",
        url = "https://mohfw.gov.in",
        educationTip = "Ayushman Bharat, COVID helpline: 1800-102-7637",
        phone = "011-23061863",
        email = "webmaster-mohfw@gov.in",
        address = "Nirman Bhawan, Maulana Azad Road, New Delhi-110011",
        twitter = "https://x.com/MoHFW_INDIA",
        facebook = "https://www.facebook.com/mohfwindia",
        officials = listOf(
            Official("Shri J P Nadda", "Minister", email = "mo.health@gov.in"),
            Official("Shri Prataprao Jadhav", "Minister of State", email = "mos.health@gov.in")
        ),
        subOrganizations = listOf(
            SubOrganization("All India Institute of Medical Sciences (AIIMS)", "https://www.aiims.edu", "Premier medical institutes across India"),
            SubOrganization("Indian Council of Medical Research (ICMR)", "https://www.icmr.gov.in", "Medical research and studies"),
            SubOrganization("Central Government Health Scheme (CGHS)", "https://cghs.nic.in", "Healthcare for central govt employees"),
            SubOrganization("National Health Mission (NHM)", "https://nhm.gov.in", "Universal healthcare access")
        ),
        category = "Health",
        functionLabel = "Healthcare & Welfare"
    ),
    MinistryContact(
        id = "home",
        name = "Ministry of Home Affairs (MHA)",
        url = "https://www.mha.gov.in",
        educationTip = "Internal security, disaster management, police",
        phone = "011-23092763",
        email = "secyhome@nic.in",
        address = "North Block, Central Secretariat, New Delhi-110001",
        twitter = "https://x.com/HMOIndia",
        facebook = "https://www.facebook.com/MinistryofHomeAffairs",
        officials = listOf(
            Official("Shri Amit Shah", "Minister", email = "hm@mha.gov.in"),
            Official("Shri Nityanand Rai", "Minister of State", email = "mos.mha@gov.in")
        ),
        subOrganizations = listOf(
            SubOrganization("Central Bureau of Investigation (CBI)", "https://cbi.gov.in", "Premier investigating agency"),
            SubOrganization("National Disaster Response Force (NDRF)", "https://ndrf.gov.in", "Disaster response and management"),
            SubOrganization("Border Security Force (BSF)", "https://bsf.gov.in", "Border guarding force"),
            SubOrganization("Central Reserve Police Force (CRPF)", "https://crpf.gov.in", "Largest paramilitary force")
        ),
        category = "Defence",
        functionLabel = "Internal Security"
    ),
    MinistryContact("housing", "Ministry of Housing (MoHUA)", "https://mohua.gov.in", "PMAY Urban Schemes.",
        category = "Other", functionLabel = "Urban Development"),
    MinistryContact("info", "Ministry of I&B", "https://mib.gov.in", "Media regulations.",
        category = "Other", functionLabel = "Media & Broadcasting"),
    MinistryContact(
        id = "jal",
        name = "Ministry of Jal Shakti",
        url = "https://jalshakti-dowr.gov.in",
        educationTip = "Jal Jeevan Mission, Swachh Bharat, water supply",
        phone = "011-23386600",
        email = "secy-mowr@nic.in",
        address = "Shram Shakti Bhawan, Rafi Marg, New Delhi-110001",
        twitter = "https://x.com/MoJS_DoWR",
        facebook = "https://www.facebook.com/MoJalShakti",
        officials = listOf(
            Official("Shri C R Paatil", "Minister", email = "jal.minister@gov.in"),
            Official("Shri V Somanna", "Minister of State", email = "mos.jalshakti@gov.in")
        ),
        subOrganizations = listOf(
            SubOrganization("Department of Water Resources, River Development & Ganga Rejuvenation", "https://jalshakti-dowr.gov.in", "River management, Namami Gange"),
            SubOrganization("Department of Drinking Water & Sanitation", "https://jalshakti-ddws.gov.in", "Jal Jeevan Mission, Swachh Bharat"),
            SubOrganization("Jal Jeevan Mission", "https://jaljeevanmission.gov.in", "Tap water connection to every rural household"),
            SubOrganization("Swachh Bharat Mission - Gramin", "https://swachhbharatmission.gov.in", "Rural sanitation, toilets, ODF")
        ),
        category = "Other",
        functionLabel = "Water & Sanitation"
    ),
    MinistryContact(
        id = "law",
        name = "Ministry of Law & Justice",
        url = "https://www.lawmin.gov.in",
        educationTip = "Legal aid, legislation, judicial appointments",
        phone = "011-23384254",
        email = "secylaw@nic.in",
        address = "Shastri Bhawan, Dr. Rajendra Prasad Road, New Delhi-110001",
        twitter = "https://x.com/LawMinIndia",
        officials = listOf(
            Official("Shri Arjun Ram Meghwal", "Minister", email = "law.minister@gov.in"),
            Official("Shri K R N Raja", "Minister of State", email = "mos.law@gov.in")
        ),
        subOrganizations = listOf(
            SubOrganization("Department of Legal Affairs", "https://lawmin.gov.in/legal-affairs", "Legislation drafting and legal advice"),
            SubOrganization("Department of Justice", "https://doj.gov.in", "Judicial appointments, legal aid"),
            SubOrganization("Legislative Department", "https://legislativedepartment.gov.in", "Vetting of Bills and ordinances"),
            SubOrganization("National Legal Services Authority (NALSA)", "https://nalsa.gov.in", "Free legal aid, Lok Adalats")
        ),
        category = "Law",
        functionLabel = "Legal & Judiciary"
    ),
    MinistryContact("rail", "Ministry of Railways", "https://indianrailways.gov.in", "RailMadad: 139",
        category = "Other", functionLabel = "Rail Transport"),
    MinistryContact("road", "Ministry of Road Transport & Highways (MoRTH)", "https://morth.nic.in", "Highway Helpline: 1033",
        category = "Other", functionLabel = "Highways & Roads"),
    // === Added from iGOD Directory ===
    MinistryContact("cooperation", "Ministry of Cooperation", "https://www.cooperation.gov.in", "Cooperative societies, PACS, dairy cooperatives.",
        category = "Other", functionLabel = "Cooperatives"),
    MinistryContact("corporate", "Ministry of Corporate Affairs (MCA)", "https://www.mca.gov.in", "Company registration, MCA21, LLP.",
        phone = "011-23046825", email = "mca21@mca.gov.in",
        category = "Finance", functionLabel = "Corporate Governance"),
    MinistryContact("culture", "Ministry of Culture", "https://culture.gov.in", "Heritage, museums, performing arts.",
        phone = "011-23388230", email = "secy.culture@nic.in",
        category = "Other", functionLabel = "Art & Heritage"),
    MinistryContact("doner", "Ministry of Development of North Eastern Region (MDONER)", "https://mdoner.gov.in", "NE region development, connectivity, tourism.",
        phone = "011-23022475", email = "secy-doner@gov.in",
        category = "Other", functionLabel = "NE Development"),
    MinistryContact("earth", "Ministry of Earth Sciences (MoES)", "https://moes.gov.in", "Weather, ocean, seismology, IMD.",
        phone = "011-24669524", email = "secy-moes@gov.in",
        category = "Other", functionLabel = "Earth Sciences"),
    MinistryContact("meity", "Ministry of Electronics & Information Technology (MeitY)", "https://meity.gov.in", "Digital India, DigiLocker, UPI, Aadhaar.",
        phone = "011-24301851", email = "webmaster@meity.gov.in",
        category = "Other", functionLabel = "IT & Digital"),
    MinistryContact("fisheries", "Ministry of Fisheries, Animal Husbandry & Dairying (MoFAHD)", "https://dahd.nic.in", "Blue Revolution, PM Matsya Sampada Yojana.",
        phone = "011-23070370", email = "secy-dahdf@gov.in",
        category = "Agriculture", functionLabel = "Fisheries & Dairy"),
    MinistryContact("foodprocess", "Ministry of Food Processing Industries (MoFPI)", "https://mofpi.gov.in", "Pradhan Mantri Kisan SAMPADA Yojana.",
        phone = "011-26492174", email = "secy-mofpi@nic.in",
        category = "Other", functionLabel = "Food Processing"),
    MinistryContact("heavyind", "Ministry of Heavy Industries (MoHI)", "https://heavyindustries.gov.in", "FAME India scheme, EV subsidies.",
        phone = "011-23062985", email = "secy-hi@gov.in",
        category = "Other", functionLabel = "Heavy Industries"),
    MinistryContact("labour", "Ministry of Labour & Employment (MoLE)", "https://labour.gov.in", "EPFO, ESIC, labour laws, minimum wages.",
        phone = "011-23710260", email = "secy-labour@gov.in",
        category = "Other", functionLabel = "Labour & Employment"),
    MinistryContact("msme", "Ministry of Micro, Small & Medium Enterprises (MSME)", "https://msme.gov.in", "Udyam registration, MUDRA loans, MSME schemes.",
        phone = "011-23062107", email = "secy-msme@nic.in",
        category = "Finance", functionLabel = "MSME & Startups"),
    MinistryContact("mines", "Ministry of Mines (MoM)", "https://mines.gov.in", "Mining policy, mineral resources.",
        phone = "011-23385017", email = "secy.mines@gov.in",
        category = "Other", functionLabel = "Mining & Minerals"),
    MinistryContact("minority", "Ministry of Minority Affairs (MoMA)", "https://minorityaffairs.gov.in", "PM VIKAS, scholarship schemes for minorities.",
        phone = "011-23583788", email = "secy.minority@gov.in",
        category = "Other", functionLabel = "Minority Welfare"),
    MinistryContact("mnre", "Ministry of New & Renewable Energy (MNRE)", "https://mnre.gov.in", "Solar, wind, PM-KUSUM, rooftop solar.",
        phone = "011-24360707", email = "secy-mnre@gov.in",
        category = "Other", functionLabel = "Renewable Energy"),
    MinistryContact("panchayat", "Ministry of Panchayati Raj (MoPR)", "https://panchayat.gov.in", "Gram panchayat governance, SVAMITVA.",
        phone = "011-23044005", email = "secy-pr@gov.in",
        category = "Other", functionLabel = "Local Governance"),
    MinistryContact("parliament", "Ministry of Parliamentary Affairs (MPA)", "https://www.mpa.gov.in", "Parliament sessions, legislative business.",
        phone = "011-23017726", email = "secy-mpa@gov.in",
        category = "Law", functionLabel = "Parliamentary Affairs"),
    MinistryContact("personnel", "Ministry of Personnel, Public Grievances & Pensions (MoPPGP)", "https://persmin.gov.in", "CPGRAMS public grievances, UPSC, CVC.",
        phone = "011-23042834", email = "secy-dopt@nic.in",
        category = "Other", functionLabel = "Personnel & Pensions"),
    MinistryContact("petroleum", "Ministry of Petroleum & Natural Gas (MoPNG)", "https://mopng.gov.in", "LPG subsidies, PAHAL, fuel prices.",
        phone = "011-23383461", email = "secy-png@gov.in",
        category = "Other", functionLabel = "Oil & Gas"),
    MinistryContact("niti", "Ministry of Planning — NITI Aayog", "https://niti.gov.in", "National policy think tank, SDG monitoring.",
        phone = "011-23042511", email = "ceo-niti@gov.in",
        category = "Other", functionLabel = "Policy & Planning"),
    MinistryContact("ports", "Ministry of Ports, Shipping & Waterways (MoPSW)", "https://shipmin.gov.in", "Sagarmala, major ports, inland waterways.",
        phone = "011-23710363", email = "secy-shipping@gov.in",
        category = "Other", functionLabel = "Ports & Shipping"),
    MinistryContact("power", "Ministry of Power", "https://powermin.gov.in", "Saubhagya, UDAY, electricity reforms.",
        phone = "011-23710271", email = "secy-power@gov.in",
        category = "Other", functionLabel = "Power & Electricity"),
    MinistryContact("rural", "Ministry of Rural Development (MoRD)", "https://rural.gov.in", "MGNREGA, PMAY-Gramin, DAY-NRLM.",
        phone = "011-23382173", email = "secy-rd@gov.in",
        category = "Other", functionLabel = "Rural Development"),
    MinistryContact("science", "Ministry of Science & Technology (MST)", "https://most.gov.in", "DST, CSIR, DBT, research funding.",
        phone = "011-26562991", email = "secy-dst@gov.in",
        category = "Education", functionLabel = "Science & Research"),
    MinistryContact("skill", "Ministry of Skill Development & Entrepreneurship (MSDE)", "https://www.msde.gov.in", "Skill India, PMKVY, ITIs.",
        phone = "011-23461656", email = "secy-msde@gov.in",
        category = "Education", functionLabel = "Skill Development"),
    MinistryContact("socialjustice", "Ministry of Social Justice & Empowerment (MoSJE)", "https://socialjustice.gov.in", "SC/OBC welfare, disability rights, anti-drug.",
        phone = "011-23384956", email = "secy-sje@nic.in",
        category = "Other", functionLabel = "Social Justice"),
    MinistryContact("statistics", "Ministry of Statistics & Programme Implementation (MoSPI)", "https://www.mospi.gov.in", "GDP data, census, national surveys.",
        phone = "011-23362843", email = "secy-mospi@gov.in",
        category = "Other", functionLabel = "Statistics & Data"),
    MinistryContact("steel", "Ministry of Steel (MoS)", "https://steel.gov.in", "Steel production, National Steel Policy.",
        phone = "011-23063321", email = "secy-steel@gov.in",
        category = "Other", functionLabel = "Steel Industry"),
    MinistryContact("textiles", "Ministry of Textiles (MoT)", "https://texmin.gov.in", "PM MITRA mega textile parks, handloom.",
        phone = "011-23061104", email = "secy-textiles@gov.in",
        category = "Other", functionLabel = "Textiles & Handloom"),
    MinistryContact("tourism", "Ministry of Tourism", "https://tourism.gov.in", "Incredible India, Swadesh Darshan 2.0.",
        phone = "011-23711995", email = "secy-tourism@gov.in",
        category = "Other", functionLabel = "Tourism"),
    MinistryContact("tribal", "Ministry of Tribal Affairs (MoTA)", "https://tribal.gov.in", "Tribal welfare, Van Dhan Vikas Kendra.",
        phone = "011-23381476", email = "secy-tribal@gov.in",
        category = "Other", functionLabel = "Tribal Welfare"),
    MinistryContact("wcd", "Ministry of Women & Child Development (MoWCD)", "https://wcd.gov.in", "Beti Bachao, POSHAN Abhiyaan, ICDS.",
        phone = "011-23381611", email = "secy-wcd@nic.in",
        category = "Other", functionLabel = "Women & Children"),
    MinistryContact("youth", "Ministry of Youth Affairs & Sports (MoYAS)", "https://yas.gov.in", "Khelo India, NSS, Nehru Yuva Kendra.",
        phone = "011-23382169", email = "secy-yas@gov.in",
        category = "Other", functionLabel = "Youth & Sports")
)
