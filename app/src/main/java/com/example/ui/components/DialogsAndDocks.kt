package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.window.Dialog
import com.example.data.PlayPulseRepository.ActiveVoiceRoomState
import com.example.model.CountryInfo
import com.example.ui.theme.*

@Composable
fun VoiceChatDock(
    voiceState: ActiveVoiceRoomState,
    onToggleMute: () -> Unit,
    onToggleDeafen: () -> Unit,
    onDisconnect: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!voiceState.isConnected) return

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.5.dp, Brush.horizontalGradient(listOf(NeonCyan, NeonGreen)), RoundedCornerShape(16.dp))
            .testTag("voice_chat_dock"),
        color = DarkSurface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(NeonGreen.copy(alpha = 0.2f))
                        .border(1.dp, NeonGreen, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (voiceState.isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Voice Status",
                        tint = if (voiceState.isMuted) Color.Red else NeonGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = voiceState.channelName,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                    Text(
                        text = "${voiceState.hubName} • ${voiceState.participants.size} squad members",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                IconButton(
                    onClick = onToggleMute,
                    modifier = Modifier.size(34.dp)
                ) {
                    Icon(
                        imageVector = if (voiceState.isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Mute",
                        tint = if (voiceState.isMuted) Color.Red else TextPrimary
                    )
                }

                IconButton(
                    onClick = onToggleDeafen,
                    modifier = Modifier.size(34.dp)
                ) {
                    Icon(
                        imageVector = if (voiceState.isDeafened) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                        contentDescription = "Deafen",
                        tint = if (voiceState.isDeafened) Color.Red else TextPrimary
                    )
                }

                IconButton(
                    onClick = onDisconnect,
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color.Red.copy(alpha = 0.2f))
                ) {
                    Icon(
                        imageVector = Icons.Default.CallEnd,
                        contentDescription = "Disconnect Voice",
                        tint = Color.Red,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AuthOAuthDialog(
    supportedCountries: List<CountryInfo>,
    onDismiss: () -> Unit,
    onLoginSuccess: (provider: String) -> Unit
) {
    var selectedCountry by remember { mutableStateOf(supportedCountries.first()) }
    var phoneNumber by remember { mutableStateOf("") }
    var isCountryDropdownOpen by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(1.5.dp, Brush.horizontalGradient(listOf(NeonCyan, NeonMagenta)), RoundedCornerShape(24.dp)),
            color = DarkSurface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // PlayPulse Neon Logo Header
                Text("⚡", fontSize = 40.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Welcome to PlayPulse",
                    color = TextPrimary,
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp
                )
                Text(
                    text = "Global Gaming Hangout & Creator Universe",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // OAuth 1-tap buttons
                OAuthProviderButton(
                    title = "Continue with Google",
                    iconEmoji = "🌐",
                    brandColor = Color(0xFF4285F4),
                    onClick = { onLoginSuccess("Google") }
                )

                Spacer(modifier = Modifier.height(10.dp))

                OAuthProviderButton(
                    title = "Continue with X (Twitter)",
                    iconEmoji = "✖️",
                    brandColor = Color(0xFF000000),
                    onClick = { onLoginSuccess("X") }
                )

                Spacer(modifier = Modifier.height(10.dp))

                OAuthProviderButton(
                    title = "Continue with Facebook",
                    iconEmoji = "🔵",
                    brandColor = Color(0xFF1877F2),
                    onClick = { onLoginSuccess("Facebook") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = DarkBorder)
                    Text(" or phone sign in ", color = TextMuted, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 8.dp))
                    HorizontalDivider(modifier = Modifier.weight(1f), color = DarkBorder)
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Country selector & Phone input
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurfaceVariant)
                        .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .clickable { isCountryDropdownOpen = true }
                            .padding(vertical = 10.dp, horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(selectedCountry.flagEmoji, fontSize = 18.sp)
                        Text(selectedCountry.dialCode, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Country", tint = TextMuted, modifier = Modifier.size(16.dp))
                    }

                    VerticalDivider(modifier = Modifier.height(24.dp).padding(horizontal = 6.dp), color = DarkBorder)

                    TextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        placeholder = { Text("Mobile number", color = TextMuted, fontSize = 13.sp) },
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
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = { onLoginSuccess("Phone") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("submit_login_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Instant One-Tap Access", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (isCountryDropdownOpen) {
        Dialog(onDismissRequest = { isCountryDropdownOpen = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
                    .clip(RoundedCornerShape(20.dp)),
                color = DarkSurface
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Select Country (${supportedCountries.size})", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyColumn {
                        items(supportedCountries) { country ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedCountry = country
                                        isCountryDropdownOpen = false
                                    }
                                    .padding(vertical = 10.dp, horizontal = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(country.flagEmoji, fontSize = 20.sp)
                                    Text(country.name, color = TextPrimary, fontSize = 14.sp)
                                }
                                Text(country.dialCode, color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                            HorizontalDivider(color = DarkBorder.copy(alpha = 0.5f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OAuthProviderButton(
    title: String,
    iconEmoji: String,
    brandColor: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() },
        color = DarkSurfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(iconEmoji, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = title,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun LanguageSelectorDialog(
    currentLanguage: String,
    onSelectLanguage: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val languages = listOf(
        "English" to "🇺🇸 English (US)",
        "Español" to "🇪🇸 Español",
        "日本語" to "🇯🇵 日本語 (Japanese)",
        "Deutsch" to "🇩🇪 Deutsch",
        "Français" to "🇫🇷 Français",
        "한국어" to "🇰🇷 한국어 (Korean)",
        "Português" to "🇧🇷 Português (Brasil)",
        "Hindi" to "🇮🇳 हिन्दी (Hindi)"
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, NeonCyan.copy(alpha = 0.6f), RoundedCornerShape(20.dp)),
            color = DarkSurface
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Select App Language", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn {
                    items(languages) { (key, display) ->
                        val isSelected = currentLanguage == key
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(
                                    1.dp,
                                    if (isSelected) NeonCyan else Color.Transparent,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    onSelectLanguage(key)
                                    onDismiss()
                                },
                            color = if (isSelected) NeonCyan.copy(alpha = 0.15f) else DarkSurfaceVariant
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(display, color = TextPrimary, fontSize = 14.sp)
                                if (isSelected) {
                                    Icon(Icons.Default.Check, contentDescription = "Selected", tint = NeonCyan)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
