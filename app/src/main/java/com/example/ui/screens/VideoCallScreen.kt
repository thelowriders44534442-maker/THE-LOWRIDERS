package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.ui.PlayPulseViewModel
import com.example.ui.components.AvatarWithNeonRing
import com.example.ui.components.NeonBadge
import com.example.ui.theme.*

@Composable
fun VideoCallScreen(
    viewModel: PlayPulseViewModel,
    modifier: Modifier = Modifier
) {
    val callSession by viewModel.currentCall.collectAsState()
    if (!callSession.isActive) return

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkVoid)
            .testTag("video_call_screen")
    ) {
        // Main Screen: Remote peer camera simulation or audio waveform
        if (callSession.isVideo) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF16222F), Color(0xFF1F1C2C), Color(0xFF0D0F18))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AvatarWithNeonRing(
                        emoji = callSession.peerAvatar,
                        size = 110.dp,
                        ringColor = NeonCyan
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = callSession.peerName,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = "Encrypted", tint = NeonGreen, modifier = Modifier.size(13.dp))
                        Text(
                            text = "PlayPulse 256-bit Encrypted Video Call",
                            color = NeonGreen,
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = callSession.networkQuality,
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }

            // Local Camera PIP (Picture-in-Picture)
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(top = 16.dp, end = 16.dp)
                    .size(width = 100.dp, height = 150.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkSurfaceVariant)
                    .border(1.5.dp, NeonMagenta, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (callSession.isCameraOff) {
                    Icon(Icons.Default.VideocamOff, contentDescription = "Cam Off", tint = TextMuted)
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("👾", fontSize = 28.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("You", color = TextSecondary, fontSize = 11.sp)
                    }
                }
            }
        } else {
            // Audio Call Screen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AvatarWithNeonRing(
                    emoji = callSession.peerAvatar,
                    size = 120.dp,
                    ringColor = NeonCyan
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = callSession.peerName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                NeonBadge(text = "ENCRYPTED VOICE COMMS", borderColor = NeonGreen)
                Spacer(modifier = Modifier.height(12.dp))
                Text("00:24 • Connected", color = TextSecondary, fontSize = 13.sp)
            }
        }

        // Bottom Controls Bar (Mute, Video toggle, Flip, End Call)
        Surface(
            color = Color.Black.copy(alpha = 0.85f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 24.dp, start = 20.dp, end = 20.dp),
            shape = RoundedCornerShape(28.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.repository.toggleCallMute() },
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(if (callSession.isMuted) Color.Red else DarkSurfaceVariant)
                ) {
                    Icon(
                        imageVector = if (callSession.isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Mute",
                        tint = Color.White
                    )
                }

                if (callSession.isVideo) {
                    IconButton(
                        onClick = { viewModel.repository.toggleCallCamera() },
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(if (callSession.isCameraOff) Color.Red else DarkSurfaceVariant)
                    ) {
                        Icon(
                            imageVector = if (callSession.isCameraOff) Icons.Default.VideocamOff else Icons.Default.Videocam,
                            contentDescription = "Camera Toggle",
                            tint = Color.White
                        )
                    }

                    IconButton(
                        onClick = { /* flip */ },
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(DarkSurfaceVariant)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlipCameraAndroid,
                            contentDescription = "Flip Camera",
                            tint = Color.White
                        )
                    }
                }

                // End Call Red Button
                IconButton(
                    onClick = { viewModel.repository.endCall() },
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color.Red)
                        .testTag("end_call_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.CallEnd,
                        contentDescription = "End Call",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }
    }
}
