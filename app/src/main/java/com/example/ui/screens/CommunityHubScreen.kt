package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.*
import com.example.ui.PlayPulseViewModel
import com.example.ui.components.AvatarWithNeonRing
import com.example.ui.components.NeonBadge
import com.example.ui.theme.*

@Composable
fun CommunityHubScreen(
    viewModel: PlayPulseViewModel,
    modifier: Modifier = Modifier
) {
    val communityHubs by viewModel.communityHubs.collectAsState()
    val selectedHubId by viewModel.selectedHubId.collectAsState()
    val selectedChannelId by viewModel.selectedChannelId.collectAsState()
    val voiceRoomState by viewModel.voiceRoomState.collectAsState()
    val isModerationSheetOpen by viewModel.isModerationSheetOpen.collectAsState()

    val currentHub = communityHubs.find { it.id == selectedHubId } ?: communityHubs.first()

    // Interactive channel messages
    var channelMessages by remember {
        mutableStateOf(
            listOf(
                ChatMessage("cm1", "u1", "GhostRider", "Anyone looking for a 3rd member for Predator ranked tonight? Need a flex player with mic.", null, "19:14", isMe = false),
                ChatMessage("cm2", "u2", "ValkQueen", "I'm down around 8 PM! Running Conduit or Bang.", "🎯", "19:16", isMe = false),
                ChatMessage("cm3", "user_me", "NeonPhantom", "Count me in! I can anchor with Wattson/Gibby.", "🔥", "19:20", isMe = true)
            )
        )
    }
    var messageText by remember { mutableStateOf("") }

    Row(
        modifier = modifier
            .fillMaxSize()
            .background(DarkVoid)
            .testTag("community_hub_screen")
    ) {
        // Left Discord-Style Server Rail (Icons)
        Column(
            modifier = Modifier
                .width(72.dp)
                .fillMaxHeight()
                .background(DarkSurfaceVariant)
                .border(1.dp, DarkBorder)
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Home / Hub icon
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = DarkSurface,
                modifier = Modifier
                    .size(48.dp)
                    .border(1.5.dp, NeonCyan, RoundedCornerShape(16.dp))
                    .clickable { viewModel.selectHub("hub_apex") }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("⚡", fontSize = 22.sp)
                }
            }

            HorizontalDivider(color = DarkBorder, modifier = Modifier.padding(horizontal = 14.dp))

            // Hubs list
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(communityHubs) { hub ->
                    val isSelected = hub.id == selectedHubId
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Discord-style active white pill bar
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .height(if (isSelected) 36.dp else 0.dp)
                                .clip(RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                                .background(if (isSelected) NeonCyan else Color.Transparent)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Surface(
                            shape = RoundedCornerShape(if (isSelected) 14.dp else 24.dp),
                            color = if (isSelected) NeonCyan.copy(alpha = 0.25f) else DarkSurface,
                            modifier = Modifier
                                .size(46.dp)
                                .border(
                                    1.dp,
                                    if (isSelected) NeonCyan else DarkBorder,
                                    RoundedCornerShape(if (isSelected) 14.dp else 24.dp)
                                )
                                .clickable { viewModel.selectHub(hub.id) }
                                .testTag("hub_icon_${hub.id}")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(hub.iconEmoji, fontSize = 22.sp)
                            }
                        }
                    }
                }
            }
        }

        // Channels & Chat Area
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(DarkSurface)
        ) {
            // Hub Header Bar
            Surface(
                color = DarkSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = currentHub.name,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            if (currentHub.verifiedPillar) {
                                Text("🛡️", fontSize = 12.sp)
                            }
                        }
                        Text(
                            text = "🟢 ${currentHub.onlineCount} online • ${currentHub.memberCount} members",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }

                    // Moderation & Rules Menu Icon
                    IconButton(
                        onClick = { viewModel.isModerationSheetOpen.value = true },
                        modifier = Modifier.testTag("hub_moderation_button")
                    ) {
                        Icon(Icons.Default.Shield, contentDescription = "Moderation", tint = NeonGreen)
                    }
                }
            }

            // Categories & Channels Accordion
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.42f)
                    .background(DarkSurfaceVariant)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                currentHub.categories.forEach { category ->
                    item {
                        Text(
                            text = category.name,
                            color = TextMuted,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
                        )
                    }

                    items(category.channels) { channel ->
                        val isSelected = channel.id == selectedChannelId
                        val isVoice = channel.type == ChannelType.VOICE

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) NeonCyan.copy(alpha = 0.15f) else Color.Transparent,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                                .clickable {
                                    if (isVoice) {
                                        viewModel.repository.joinVoiceChannel(currentHub, channel)
                                    } else {
                                        viewModel.selectChannel(channel.id)
                                    }
                                }
                        ) {
                            Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = if (isVoice) "🔊" else "#",
                                            color = if (isSelected) NeonCyan else TextSecondary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                        Text(
                                            text = channel.name,
                                            color = if (isSelected) TextPrimary else TextSecondary,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 13.sp
                                        )
                                    }

                                    if (isVoice) {
                                        NeonBadge(
                                            text = "JOIN VOICE",
                                            borderColor = NeonGreen,
                                            containerColor = NeonGreen.copy(alpha = 0.15f)
                                        )
                                    } else if (channel.unreadMessagesCount > 0) {
                                        Surface(shape = CircleShape, color = NeonMagenta) {
                                            Text(
                                                text = "${channel.unreadMessagesCount}",
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }

                                // Show active speakers inside voice channel (Discord style)
                                if (isVoice && channel.activeVoiceUsers.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Column(
                                        modifier = Modifier.padding(start = 16.dp),
                                        verticalArrangement = Arrangement.spacedBy(3.dp)
                                    ) {
                                        channel.activeVoiceUsers.forEach { user ->
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(8.dp)
                                                        .clip(CircleShape)
                                                        .background(if (user.isSpeaking) NeonGreen else TextMuted)
                                                )
                                                Text(user.avatarEmoji, fontSize = 11.sp)
                                                Text(
                                                    text = user.name,
                                                    color = if (user.isSpeaking) NeonGreen else TextSecondary,
                                                    fontSize = 11.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            HorizontalDivider(color = DarkBorder)

            // Active Channel Text Feed (Bottom Half)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.58f)
                    .background(DarkSurface)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    reverseLayout = true
                ) {
                    items(channelMessages.reversed()) { msg ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            AvatarWithNeonRing(
                                emoji = if (msg.isMe) "👾" else "👤",
                                size = 32.dp,
                                ringColor = if (msg.isMe) NeonCyan else NeonPurple
                            )
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(msg.senderName, color = if (msg.isMe) NeonCyan else TextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text(msg.timestamp, color = TextMuted, fontSize = 10.sp)
                                }
                                Text(msg.text, color = TextPrimary, fontSize = 13.sp)
                                if (msg.stickerEmoji != null) {
                                    Text(msg.stickerEmoji, fontSize = 20.sp, modifier = Modifier.padding(top = 2.dp))
                                }
                            }
                        }
                    }
                }

                // Chat Input bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(DarkSurfaceVariant)
                        .border(1.dp, DarkBorder, RoundedCornerShape(20.dp))
                        .padding(horizontal = 10.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            channelMessages = channelMessages + ChatMessage(
                                id = "cm_${System.currentTimeMillis()}",
                                senderId = "user_me",
                                senderName = "NeonPhantom",
                                text = "GG! Let's get that win!",
                                stickerEmoji = "🔥",
                                timestamp = "Just now",
                                isMe = true
                            )
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Text("🔥", fontSize = 16.sp)
                    }

                    TextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        placeholder = { Text("Message #general-lounge...", color = TextMuted, fontSize = 12.sp) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = {
                            if (messageText.isNotBlank()) {
                                channelMessages = channelMessages + ChatMessage(
                                    id = "cm_${System.currentTimeMillis()}",
                                    senderId = "user_me",
                                    senderName = "NeonPhantom",
                                    text = messageText,
                                    timestamp = "Just now",
                                    isMe = true
                                )
                                messageText = ""
                            }
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = NeonCyan, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }

    // Community Moderation & Rules Modal
    if (isModerationSheetOpen) {
        Dialog(onDismissRequest = { viewModel.isModerationSheetOpen.value = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.5.dp, NeonGreen, RoundedCornerShape(20.dp)),
                color = DarkSurface
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        NeonBadge(text = "COMMUNITY MODERATION & RULES", borderColor = NeonGreen)
                        IconButton(onClick = { viewModel.isModerationSheetOpen.value = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(currentHub.description, color = TextSecondary, fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(14.dp))
                    Text("Server Guidelines", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))

                    currentHub.rules.forEachIndexed { index, rule ->
                        Row(modifier = Modifier.padding(vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("${index + 1}.", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text(rule, color = TextPrimary.copy(alpha = 0.9f), fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Moderator Actions", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = { viewModel.isModerationSheetOpen.value = false },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Report User", fontSize = 11.sp, color = Color.Red)
                        }
                        Button(
                            onClick = { viewModel.isModerationSheetOpen.value = false },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Clear Chat", fontSize = 11.sp, color = TextPrimary)
                        }
                    }
                }
            }
        }
    }
}
