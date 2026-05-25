package com.nivar.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import com.nivar.app.ui.theme.NivarNavy
import com.nivar.app.ui.theme.NivarSky
import com.nivar.app.ui.theme.NivarIce
import com.nivar.app.ui.theme.PureWhite
import kotlinx.coroutines.launch

// ===== Data Model =====
data class ChatMessage(
    val id: String,
    val text: String,
    val isUser: Boolean,
    val options: List<String> = emptyList()
)

// ===== Civic AI Assistant Screen =====
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentScreen() {
    val listState = androidx.compose.foundation.lazy.rememberLazyListState()
    var messages by remember { mutableStateOf(listOf(
        ChatMessage(
            "1",
            "Hello! I'm your Civic AI assistant. I can help with RTI drafts, legal rights, finding officials, or filing grievances. How can I help?",
            false,
            listOf("Draft RTI", "Know My Rights", "Find Official", "File Complaint")
        )
    )) }
    var isTyping by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(messages.size, isTyping) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - (if (isTyping) 0 else 1))
        }
    }

    fun handleAgentResponse(option: String) {
        scope.launch {
            isTyping = true
            kotlinx.coroutines.delay(1200)
            isTyping = false

            val response = when {
                option.contains("RTI", true) ->
                    ChatMessage(java.util.UUID.randomUUID().toString(),
                        "I'll help draft your RTI request. First, which public authority should receive this?\n\nBased on your location (Ward 12, Indiranagar), I recommend:\n1. BBMP Commissioner\n2. Ward Corporator",
                        false, listOf("BBMP Commissioner", "Ward Corporator", "Other"))
                option.contains("Rights", true) ->
                    ChatMessage(java.util.UUID.randomUUID().toString(),
                        "Here are your key fundamental rights:\n\n📜 Article 14 — Right to Equality\n📜 Article 19 — Freedom of Speech\n📜 Article 21 — Right to Life & Liberty\n\nWould you like me to explain any of these in detail?",
                        false, listOf("Article 14", "Article 19", "Article 21"))
                option.contains("Accident", true) ->
                    ChatMessage(java.util.UUID.randomUUID().toString(),
                        "Creating Critical Incident Report. Is anyone injured?",
                        false, listOf("Yes, Critical", "Yes, Minor", "No, Damage Only"))
                option == "Yes, Critical" ->
                    ChatMessage(java.util.UUID.randomUUID().toString(),
                        "🚨 EMERGENCY PROTOCOL ACTIVE\n\n1. Call 108 immediately.\n2. Do not move the victim.\n3. Apply pressure to bleeding.",
                        false, listOf("Called 108", "Need Police"))
                option == "Called 108" ->
                    ChatMessage(java.util.UUID.randomUUID().toString(),
                        "Good. You are protected by the Good Samaritan Law (2016).\n\nNo hospital or police can force you to stay or pay for the victim's initial care.",
                        false, listOf("Check Rights", "Done"))
                else ->
                    ChatMessage(java.util.UUID.randomUUID().toString(),
                        "I have noted your request for '$option'. Our team will process this shortly.",
                        false, listOf("Main Menu"))
            }
            messages = messages + response
        }
    }

    val documentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            messages = messages + ChatMessage(java.util.UUID.randomUUID().toString(), "Attached document: ${uri.lastPathSegment ?: "File"}", true)
            handleAgentResponse("Attached document")
        }
    }





    fun onOptionSelected(option: String) {
        messages = messages + ChatMessage(java.util.UUID.randomUUID().toString(), option, true)
        handleAgentResponse(option)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // AI Avatar
                    Surface(
                        shape = CircleShape,
                        color = NivarIce,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Icon(
                                Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = NivarNavy,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            "Civic AI Assistant",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = NivarNavy
                        )
                        Text(
                            "Powered by Gemini • Legal + Governance Expert",
                            style = MaterialTheme.typography.labelMedium,
                            color = NivarSky
                        )
                    }
                }
            }
        }

        // Chat Messages
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(messages) { msg ->
                ChatBubble(message = msg, onOptionClick = { onOptionSelected(it) })
            }

            if (isTyping) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = NivarIce,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                Icon(Icons.Default.AutoAwesome, null, tint = NivarNavy, modifier = Modifier.size(16.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp)
                        ) {
                            Text(
                                "Thinking...",
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                style = MaterialTheme.typography.bodySmall,
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Input Area
        Surface(
            tonalElevation = 2.dp,
            shadowElevation = 4.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Suggestion Chips
                if (messages.isNotEmpty() && !messages.last().isUser && messages.last().options.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        messages.last().options.forEach { option ->
                            SuggestionChip(
                                onClick = { onOptionSelected(option) },
                                label = {
                                    Text(option, style = MaterialTheme.typography.labelMedium)
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = NivarIce
                                )
                            )
                        }
                    }
                }

                // Text Input Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Attachment button
                    IconButton(onClick = { documentLauncher.launch("*/*") }, modifier = Modifier.size(40.dp)) {
                        Icon(
                            Icons.Default.AttachFile,
                            contentDescription = "Attach",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text("Type your query...", style = MaterialTheme.typography.bodyMedium) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedBorderColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    // Voice input
                    IconButton(onClick = { /* TODO */ }, modifier = Modifier.size(40.dp)) {
                        Icon(
                            Icons.Default.Mic,
                            contentDescription = "Voice",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Send button
                    FilledIconButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                val text = inputText
                                inputText = ""
                                onOptionSelected(text)
                            }
                        },
                        modifier = Modifier.size(44.dp),
                        enabled = inputText.isNotBlank(),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = NivarNavy,
                            contentColor = PureWhite
                        )
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send")
                    }
                }
            }
        }
    }
}

// ===== Chat Bubble Component =====
@Composable
fun ChatBubble(message: ChatMessage, onOptionClick: (String) -> Unit) {
    val scaleAnim = remember { Animatable(0.85f) }
    val alphaAnim = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch { scaleAnim.animateTo(1f, tween(250, easing = LinearOutSlowInEasing)) }
        launch { alphaAnim.animateTo(1f, tween(250)) }
    }

    Column(
        horizontalAlignment = if (message.isUser) Alignment.End else Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scaleAnim.value
                scaleY = scaleAnim.value
                alpha = alphaAnim.value
            }
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Bot avatar (left side for bot messages)
            if (!message.isUser) {
                Surface(
                    shape = CircleShape,
                    color = NivarIce,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Icon(Icons.Default.AutoAwesome, null, tint = NivarNavy, modifier = Modifier.size(18.dp))
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            Surface(
                color = if (message.isUser) NivarNavy else MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (message.isUser) 16.dp else 4.dp,
                    bottomEnd = if (message.isUser) 4.dp else 16.dp
                ),
                shadowElevation = 1.dp,
                modifier = Modifier.widthIn(max = 280.dp)
            ) {
                Text(
                    text = message.text,
                    modifier = Modifier.padding(14.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (message.isUser) PureWhite else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
