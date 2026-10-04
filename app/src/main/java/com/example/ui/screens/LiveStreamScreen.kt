package com.example.ui.screens

import androidx.compose.animation.*
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
import com.example.model.LiveChatMessage
import com.example.ui.PlayPulseViewModel
import com.example.ui.components.AvatarWithNeonRing
import com.example.ui.components.NeonBadge
import com.example.ui.theme.*

@Composable
fun LiveStreamScreen(
    viewModel: PlayPulseViewModel,
    modifier: Modifier = Modifier
) {
    val liveStream by viewModel.activeLiveStream.collectAsState()
    val hostState by viewModel.repository.hostStreamState.collectAsState()
    val isHostModalOpen by viewModel.isHostGoLiveOpen.collectAsState()
    var chatInput by remember { mutableStateOf("") }
    var floatingHeartsCount by remember { mutableIntStateOf(0) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkVoid)
            .testTag("live_stream_screen")
    ) {
        // Stream Background & Simulated Live Video Gameplay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF0F172A), Color(0xFF1E1B4B), Color(0xFF311042))
                    )
                )
        ) {
            // Live Gameplay center visual
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 120.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.5f),
                    modifier = Modifier.size(76.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🔴", fontSize = 36.sp)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "VALORANT CHAMPIONS TOUR • FINALS",
                    color = NeonCyan,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Text(
                    text = "Broadcast bitrate: 8500 Kbps • 1080p60",
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
        }

        // Top Streamer Header Bar (TikTok Live Style)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Streamer Info Pill
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color.Black.copy(alpha = 0.65f),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonMagenta)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AvatarWithNeonRing(
                        emoji = liveStream.streamerAvatar,
                        size = 34.dp,
                        ringColor = NeonMagenta
                    )
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(liveStream.streamerName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("🔴 LIVE", color = Color.Red, fontWeight = FontWeight.Black, fontSize = 10.sp)
                        }
                        Text("👁️ ${liveStream.viewersCount} viewers", color = TextSecondary, fontSize = 10.sp)
                    }

                    Button(
                        onClick = { viewModel.repository.toggleFollowCreator("creator_val") },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(28.dp)
                    ) {
                        Text("Follow", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Host Go Live Button / Toggle
            IconButton(
                onClick = { viewModel.isHostGoLiveOpen.value = true },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Brush.horizontalGradient(listOf(NeonMagenta, NeonPurple)))
                    .testTag("go_live_studio_button")
            ) {
                Icon(Icons.Default.Videocam, contentDescription = "Host Stream", tint = Color.White)
            }
        }

        // Real-Time Animated Gift Banner (TikTok Live style)
        if (liveStream.recentGifts.isNotEmpty()) {
            val topGift = liveStream.recentGifts.first()
            AnimatedVisibility(
                visible = true,
                enter = slideInHorizontally() + fadeIn(),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 110.dp, start = 14.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.Black.copy(alpha = 0.8f),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(topGift.gift.highlightColor))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(topGift.gift.emoji, fontSize = 28.sp)
                        Column {
                            Text(topGift.senderName, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("Sent ${topGift.gift.name}", color = Color(topGift.gift.highlightColor), fontSize = 11.sp)
                        }
                        Surface(
                            shape = CircleShape,
                            color = NeonYellow,
                            modifier = Modifier.padding(start = 4.dp)
                        ) {
                            Text(
                                text = "x${topGift.comboCount}",
                                color = Color.Black,
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        // Live Chat Stream Overlay (Bottom Left)
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(0.82f)
                .padding(start = 14.dp, bottom = 80.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .heightIn(max = 240.dp)
                    .fillMaxWidth(),
                reverseLayout = true
            ) {
                items(liveStream.chatMessages.reversed(), key = { it.id }) { chat ->
                    LiveChatMessageItem(chat = chat)
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }

        // Floating Hearts when viewer taps heart button
        if (floatingHeartsCount > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 24.dp, bottom = 140.dp)
            ) {
                Text("💖", fontSize = 32.sp)
            }
        }

        // Bottom Controls Bar: Chat Input, Heart Tap, Gift Button
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp)
                .navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Chat Input
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Black.copy(alpha = 0.6f))
                    .border(1.dp, DarkBorder, RoundedCornerShape(20.dp))
                    .padding(horizontal = 12.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = chatInput,
                    onValueChange = { chatInput = it },
                    placeholder = { Text("Send comment...", color = TextMuted, fontSize = 12.sp) },
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
                        if (chatInput.isNotBlank()) {
                            viewModel.repository.sendLiveChatMessage(chatInput)
                            chatInput = ""
                        }
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = NeonCyan, modifier = Modifier.size(16.dp))
                }
            }

            // Quick Heart Burst Button
            IconButton(
                onClick = { floatingHeartsCount++ },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(NeonMagenta.copy(alpha = 0.25f))
                    .border(1.dp, NeonMagenta, CircleShape)
            ) {
                Icon(Icons.Default.Favorite, contentDescription = "Love stream", tint = NeonMagenta, modifier = Modifier.size(22.dp))
            }

            // Live Gift Launcher Button (TikTok Live Style)
            IconButton(
                onClick = { viewModel.openGiftSheet() },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Brush.horizontalGradient(listOf(NeonYellow, NeonOrange)))
                    .testTag("open_live_gifts_button")
            ) {
                Text("🎁", fontSize = 22.sp)
            }
        }
    }

    // Host Go Live Simulator Modal
    if (isHostModalOpen) {
        Dialog(onDismissRequest = { viewModel.isHostGoLiveOpen.value = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.5.dp, NeonMagenta, RoundedCornerShape(24.dp)),
                color = DarkSurface
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        NeonBadge(text = "CREATOR BROADCAST STUDIO", borderColor = NeonMagenta)
                        IconButton(onClick = { viewModel.isHostGoLiveOpen.value = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Simulated Camera Feed Preview
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.verticalGradient(listOf(Color(0xFF1F1C2C), Color(0xFF928DAB)))
                            )
                            .border(1.dp, NeonCyan, RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Videocam, contentDescription = "Camera", tint = NeonCyan, modifier = Modifier.size(44.dp))
                            Text("1080p 60fps • Ultra Low Latency RTMP", color = Color.White, fontSize = 11.sp)
                        }

                        // Live indicator badge
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color.Red,
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(10.dp)
                        ) {
                            Text("ON AIR", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = hostState.title,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("👁️ ${hostState.viewerCount}", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text("Live Viewers", color = TextMuted, fontSize = 11.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🎁 ${hostState.giftsReceived}", color = NeonYellow, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text("Gifts Received", color = TextMuted, fontSize = 11.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("⏱️ 02:25", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text("Stream Time", color = TextMuted, fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { /* switch camera */ },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.FlipCameraAndroid, contentDescription = "Flip", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Flip Cam", fontSize = 12.sp)
                        }

                        Button(
                            onClick = { viewModel.isHostGoLiveOpen.value = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("End Stream", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LiveChatMessageItem(chat: LiveChatMessage) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (chat.isHighlighted) NeonMagenta.copy(alpha = 0.25f) else Color.Black.copy(alpha = 0.6f),
        border = if (chat.isHighlighted) androidx.compose.foundation.BorderStroke(1.dp, NeonMagenta) else null
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (chat.senderBadge != null) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = NeonYellow.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonYellow)
                ) {
                    Text(
                        text = chat.senderBadge,
                        color = NeonYellow,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }
            }
            Text(
                text = "${chat.senderName}: ",
                color = NeonCyan,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
            Text(
                text = chat.message,
                color = Color.White,
                fontSize = 12.sp
            )
        }
    }
}
