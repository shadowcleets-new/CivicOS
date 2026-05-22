package com.nivar.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nivar.app.ui.theme.NivarNavy
import com.nivar.app.ui.theme.NivarIce
import com.nivar.app.ui.theme.NivarSky

import com.nivar.app.data.model.ConstitutionRepository
import com.nivar.app.data.model.ConstitutionArticle
import com.nivar.app.data.model.Language

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConstitutionScreen() {
    var selectedLanguage by remember { mutableStateOf(Language.ENG) }
    var isSimplified by remember { mutableStateOf(false) }
    var expandLanguageMenu by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val displayLanguage = if (isSimplified) {
        when (selectedLanguage) {
            Language.ENG -> Language.ENG_SIMPLE
            Language.HIN -> Language.HIN_SIMPLE
            Language.GUJ -> Language.GUJ_SIMPLE
            Language.TAM -> Language.TAM_SIMPLE
            Language.ODI -> Language.ODI_SIMPLE
            else -> selectedLanguage
        }
    } else {
        selectedLanguage
    }

    // Filter articles based on search
    val filteredArticles = if (searchQuery.isBlank()) {
        ConstitutionRepository.articles
    } else {
        ConstitutionRepository.articles.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
            it.id.contains(searchQuery, ignoreCase = true) ||
            it.part.contains(searchQuery, ignoreCase = true)
        }
    }

    // Group articles by part for accordion sections
    val groupedArticles = filteredArticles.groupBy { it.part }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header Section
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                Text(
                    text = "Know your Laws, Rights & Duties",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = NivarNavy
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Explore the Indian Constitution in plain language",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Search bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text("Search articles, amendments...", style = MaterialTheme.typography.bodyMedium)
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Controls Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Language Selector
                    Box {
                        OutlinedButton(
                            onClick = { expandLanguageMenu = true },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(selectedLanguage.displayName, style = MaterialTheme.typography.labelLarge)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.size(18.dp))
                        }
                        DropdownMenu(
                            expanded = expandLanguageMenu,
                            onDismissRequest = { expandLanguageMenu = false }
                        ) {
                            val baseLanguages = listOf(Language.ENG, Language.HIN, Language.GUJ, Language.TAM, Language.ODI)
                            baseLanguages.forEach { lang ->
                                DropdownMenuItem(
                                    text = { Text(lang.displayName) },
                                    onClick = {
                                        selectedLanguage = lang
                                        expandLanguageMenu = false
                                    }
                                )
                            }
                        }
                    }

                    // Simplify Toggle
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Simplify", style = MaterialTheme.typography.labelLarge)
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(
                            checked = isSimplified,
                            onCheckedChange = { isSimplified = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                                checkedTrackColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            }
        }

        // Accordion Content
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            groupedArticles.forEach { (part, articles) ->
                item {
                    ConstitutionAccordionSection(
                        sectionTitle = part,
                        articles = articles,
                        displayLanguage = displayLanguage,
                        isSimpleMode = isSimplified && selectedLanguage == Language.ENG
                    )
                }
            }
        }
    }
}

// ===== Accordion Section =====
@Composable
fun ConstitutionAccordionSection(
    sectionTitle: String,
    articles: List<ConstitutionArticle>,
    displayLanguage: Language,
    isSimpleMode: Boolean
) {
    var isExpanded by remember { mutableStateOf(true) }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            // Section Header (clickable to expand/collapse)
            Surface(
                onClick = { isExpanded = !isExpanded },
                color = if (isSimpleMode) NivarIce.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        if (isSimpleMode) {
                            Icon(Icons.Default.List, contentDescription = null, tint = NivarSky, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(
                            sectionTitle,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Icon(
                        if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Collapsible articles
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    articles.forEach { article ->
                        val content = article.content[displayLanguage] ?: article.content[Language.ENG] ?: "Translation not available"
                        ConstitutionArticleItem(article, content, isSimpleMode)
                        if (article != articles.last()) {
                            Divider(
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ===== Article Item =====
@Composable
fun ConstitutionArticleItem(article: ConstitutionArticle, content: String, isSimpleMode: Boolean) {
    var showFullContent by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${article.id}: ${article.title}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

            // Ask AI Button
            Surface(
                onClick = { /* TODO: Navigate to AI with this article context */ },
                shape = RoundedCornerShape(16.dp),
                color = NivarIce.copy(alpha = 0.5f),
                modifier = Modifier.height(28.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = NivarNavy,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Ask AI",
                        style = MaterialTheme.typography.labelSmall,
                        color = NivarNavy,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = if (isSimpleMode) 24.sp else 20.sp,
            maxLines = if (showFullContent) Int.MAX_VALUE else 3
        )

        if (content.length > 150) {
            Text(
                text = if (showFullContent) "Show less" else "Read more",
                style = MaterialTheme.typography.labelMedium,
                color = NivarSky,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .clickable { showFullContent = !showFullContent }
                    .padding(top = 4.dp)
            )
        }
    }
}

// Backward compatible function
@Composable
fun ConstitutionCard(article: ConstitutionArticle, content: String, isSimpleMode: Boolean) {
    ConstitutionArticleItem(article, content, isSimpleMode)
}
