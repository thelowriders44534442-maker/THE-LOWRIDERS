package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.model.ChatMessage
import com.example.model.DmConversation
import com.example.ui.PlayPulseViewModel
import com.example.ui.components.AvatarWithNeonRing
import com.example.ui.components.NeonBadge
import com.example.ui.theme.*

@Composable
fun DmMessagingScreen(
    conversation: DmConversation,
    viewModel: PlayPulseViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var messageText by remember { mutableStateOf("") }
    var selectedSticker by remember { mutableStateOf<String?>(null) }
    var isStickerPickerOpen by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkVoid)
            .testTag("dm_chat_screen")
    ) {
        // Top Encrypted DM Bar with Audio & Video Call buttons
        Surface(
            color = DarkSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }

                    AvatarWithNeonRing(
                        emoji = conversation.peerUser.avatarEmoji,
                        size = 38.dp,
                        ringColor = NeonCyan,
                        isOnline = conversation.isOnline
                    )

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = conversation.peerUser.displayName,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Default.Lock, contentDescription = "Encrypted", tint = NeonGreen, modifier = Modifier.size(11.dp))
                            Text("End-to-End Encrypted", color = NeonGreen, fontSize = 10.sp)
                        }
                    }
                }

                // Voice Call & Video Call in DM
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(
                        onClick = { viewModel.repository.startCall(conversation.peerUser, isVideo = false) },
                        modifier = Modifier.testTag("start_audio_call_button")
                    ) {
                        Icon(Icons.Default.Call, contentDescription = "Audio Call", tint = NeonCyan)
                    }

                    IconButton(
                        onClick = { viewModel.repository.startCall(conversation.peerUser, isVideo = true) },
                        modifier = Modifier.testTag("start_video_call_button")
                    ) {
                        Icon(Icons.Default.Videocam, contentDescription = "Video Call", tint = NeonMagenta)
                    }
                }
            }
        }

        // Encryption Guarantee Banner
        Surface(
            color = DarkSurfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.Shield, contentDescription = "Verified", tint = NeonCyan, modifier = Modifier.size(16.dp))
                Text(
                    text = "Direct messages & calls are end-to-end encrypted with PlayPulse Zero-Knowledge P2P.",
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
        }

        // Messages Flow
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            reverseLayout = true
        ) {
            items(conversation.messages.reversed(), key = { it.id }) { msg ->
                DmMessageBubble(msg = msg)
                Spacer(modifier = Modifier.height(6.dp))
            }
        }

        // Sticker Picker Drawer
        AnimatedVisibility(visible = isStickerPickerOpen) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurfaceVariant)
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text("Gaming Stickers & Reactions", color = TextSecondary, fontSize = 11.sp)
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(viewModel.availableStickers) { sticker ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (selectedSticker == sticker.emoji) NeonCyan.copy(alpha = 0.2f) else DarkSurface,
                            modifier = Modifier
                                .border(
                                    1.dp,
                                    if (selectedSticker == sticker.emoji) NeonCyan else Color.Transparent,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    selectedSticker = if (selectedSticker == sticker.emoji) null else sticker.emoji
                                }
                        ) {
                            Text(
                                text = sticker.emoji,
                                fontSize = 26.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        // Bottom Input Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp)
                .navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(DarkSurfaceVariant)
                    .border(1.dp, DarkBorder, RoundedCornerShape(24.dp))
                    .padding(horizontal = 10.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { isStickerPickerOpen = !isStickerPickerOpen },
                    modifier = Modifier.size(36.dp)
                ) {
                    Text(text = if (selectedSticker != null) selectedSticker!! else "👾", fontSize = 18.sp)
                }

                TextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = {
                        Text(
                            text = if (selectedSticker != null) "Send sticker with note..." else "Encrypted message...",
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                    },
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
                        if (messageText.isNotBlank() || selectedSticker != null) {
                            viewModel.repository.sendDmMessage(conversation.id, messageText, selectedSticker)
                            messageText = ""
                            selectedSticker = null
                            isStickerPickerOpen = false
                        }
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("send_dm_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = if (messageText.isNotBlank() || selectedSticker != null) NeonCyan else TextMuted
                    )
                }
            }
        }
    }
}

@Composable
fun DmMessageBubble(msg: ChatMessage) {
    val alignment = if (msg.isMe) Alignment.End else Alignment.Start
    val bubbleColor = if (msg.isMe) NeonCyan.copy(alpha = 0.2f) else DarkSurfaceVariant
    val borderColor = if (msg.isMe) NeonCyan.copy(alpha = 0.5f) else DarkBorder

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (msg.isMe) 16.dp else 4.dp,
                bottomEnd = if (msg.isMe) 4.dp else 16.dp
            ),
            color = bubbleColor,
            border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                if (msg.stickerEmoji != null) {
                    Text(text = msg.stickerEmoji, fontSize = 32.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                }
                if (msg.text.isNotBlank()) {
                    Text(
                        text = msg.text,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = msg.timestamp, color = TextMuted, fontSize = 10.sp)
                    Icon(Icons.Default.DoneAll, contentDescription = "Delivered", tint = NeonGreen, modifier = Modifier.size(12.dp))
                }
            }
        }
    }
}
