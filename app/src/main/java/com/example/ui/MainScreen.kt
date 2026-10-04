package com.example.ui

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
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: PlayPulseViewModel,
    modifier: Modifier = Modifier
) {
    val currentTab by viewModel.currentTab.collectAsState()
    val activeCommentsVideoId by viewModel.activeCommentsVideoId.collectAsState()
    val isGiftSheetOpen by viewModel.isGiftSheetOpen.collectAsState()
    val isAuthModalOpen by viewModel.isAuthModalOpen.collectAsState()
    val isLangModalOpen by viewModel.isLanguageModalOpen.collectAsState()
    val isDmsListOpen by viewModel.isDmsListOpen.collectAsState()
    val activeDmId by viewModel.activeDmId.collectAsState()
    val dmConversations by viewModel.dmConversations.collectAsState()
    val currentCall by viewModel.currentCall.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val voiceRoomState by viewModel.voiceRoomState.collectAsState()
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val commentsMap by viewModel.commentsMap.collectAsState()

    val activeConversation = dmConversations.find { it.id == activeDmId }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DarkVoid,
        topBar = {
            // Top App Bar (shown except when in active DM conversation or watching fullscreen short)
            if (activeConversation == null && currentTab != NavigationTab.SHORTS) {
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
                        // PlayPulse Brand Title with glowing badge
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.clickable { viewModel.setTab(NavigationTab.WATCH) }
                        ) {
                            AvatarWithNeonRing(
                                emoji = "⚡",
                                size = 32.dp,
                                ringColor = NeonCyan
                            )
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = "PlayPulse",
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 18.sp
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = NeonMagenta
                                    ) {
                                        Text(
                                            text = "LIVE",
                                            color = Color.White,
                                            fontWeight = FontWeight.Black,
                                            fontSize = 9.sp,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "Gaming Video & Social Hangout",
                                    color = TextMuted,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        // Top Action Icons: Language, DMs, OAuth Login
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            // Language Switcher
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = DarkSurfaceVariant,
                                modifier = Modifier.clickable { viewModel.isLanguageModalOpen.value = true }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(Icons.Default.Language, contentDescription = "Language", tint = NeonCyan, modifier = Modifier.size(14.dp))
                                    Text(currentLanguage.take(3), color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            // Direct Messages Action
                            IconButton(
                                onClick = { viewModel.openDmsList() },
                                modifier = Modifier.testTag("dms_top_bar_button")
                            ) {
                                BadgedBox(badge = {
                                    Badge(containerColor = NeonMagenta) {
                                        Text("1", color = Color.White)
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.ChatBubble,
                                        contentDescription = "Encrypted DMs",
                                        tint = TextPrimary
                                    )
                                }
                            }

                            // OAuth Sign-In Account Button
                            IconButton(
                                onClick = { viewModel.isAuthModalOpen.value = true },
                                modifier = Modifier.testTag("auth_account_button")
                            ) {
                                Icon(Icons.Default.AccountCircle, contentDescription = "Login", tint = NeonCyan)
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            // Discord & TikTok combined bottom navigation bar
            if (activeConversation == null) {
                Column {
                    // Floating persistent Voice Room dock if connected
                    VoiceChatDock(
                        voiceState = voiceRoomState,
                        onToggleMute = { viewModel.repository.toggleVoiceMute() },
                        onToggleDeafen = { viewModel.repository.toggleVoiceDeafen() },
                        onDisconnect = { viewModel.repository.leaveVoiceChannel() }
                    )

                    NavigationBar(
                        containerColor = DarkSurface,
                        contentColor = TextPrimary,
                        tonalElevation = 4.dp,
                        modifier = Modifier.testTag("bottom_nav_bar")
                    ) {
                        NavigationBarItem(
                            selected = currentTab == NavigationTab.WATCH,
                            onClick = { viewModel.setTab(NavigationTab.WATCH) },
                            icon = { Icon(Icons.Default.PlayCircle, contentDescription = "Watch") },
                            label = { Text("Watch", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = NeonCyan,
                                selectedTextColor = NeonCyan,
                                indicatorColor = NeonCyan.copy(alpha = 0.2f),
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted
                            )
                        )

                        NavigationBarItem(
                            selected = currentTab == NavigationTab.SHORTS,
                            onClick = { viewModel.setTab(NavigationTab.SHORTS) },
                            icon = { Icon(Icons.Default.FlashOn, contentDescription = "Pulse Clips") },
                            label = { Text("Pulse", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = NeonMagenta,
                                selectedTextColor = NeonMagenta,
                                indicatorColor = NeonMagenta.copy(alpha = 0.2f),
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted
                            )
                        )

                        NavigationBarItem(
                            selected = currentTab == NavigationTab.HUBS,
                            onClick = { viewModel.setTab(NavigationTab.HUBS) },
                            icon = { Icon(Icons.Default.Groups, contentDescription = "Hubs") },
                            label = { Text("Hubs", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = NeonPurple,
                                selectedTextColor = NeonPurple,
                                indicatorColor = NeonPurple.copy(alpha = 0.2f),
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted
                            )
                        )

                        NavigationBarItem(
                            selected = currentTab == NavigationTab.LIVE,
                            onClick = { viewModel.setTab(NavigationTab.LIVE) },
                            icon = { Icon(Icons.Default.LiveTv, contentDescription = "Live") },
                            label = { Text("Live", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.Red,
                                selectedTextColor = Color.Red,
                                indicatorColor = Color.Red.copy(alpha = 0.2f),
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted
                            )
                        )

                        NavigationBarItem(
                            selected = currentTab == NavigationTab.PROFILE,
                            onClick = { viewModel.setTab(NavigationTab.PROFILE) },
                            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                            label = { Text("Profile", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = NeonGreen,
                                selectedTextColor = NeonGreen,
                                indicatorColor = NeonGreen.copy(alpha = 0.2f),
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main Screen Routing
            if (activeConversation != null) {
                DmMessagingScreen(
                    conversation = activeConversation,
                    viewModel = viewModel,
                    onBack = { viewModel.closeDmChat() }
                )
            } else {
                when (currentTab) {
                    NavigationTab.WATCH -> FeedScreen(viewModel = viewModel)
                    NavigationTab.SHORTS -> PulseShortsScreen(viewModel = viewModel)
                    NavigationTab.HUBS -> CommunityHubScreen(viewModel = viewModel)
                    NavigationTab.LIVE -> LiveStreamScreen(viewModel = viewModel)
                    NavigationTab.PROFILE -> ProfileAndStudioScreen(viewModel = viewModel)
                }
            }

            // Real-Time Video Call Overlay
            if (currentCall.isActive) {
                VideoCallScreen(viewModel = viewModel)
            }
        }
    }

    // Direct Messages List Dialog
    if (isDmsListOpen) {
        Dialog(onDismissRequest = { viewModel.closeDmsList() }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, NeonCyan, RoundedCornerShape(20.dp)),
                color = DarkSurface
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Direct Messages", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            NeonBadge(text = "E2E ENCRYPTED", borderColor = NeonGreen)
                        }
                        IconButton(onClick = { viewModel.closeDmsList() }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyColumn {
                        items(dmConversations) { conv ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = DarkSurfaceVariant,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        viewModel.openDmChat(conv.id)
                                        viewModel.closeDmsList()
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    AvatarWithNeonRing(
                                        emoji = conv.peerUser.avatarEmoji,
                                        size = 42.dp,
                                        ringColor = NeonCyan,
                                        isOnline = conv.isOnline
                                    )

                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(conv.peerUser.displayName, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                            Text(conv.timestamp, color = TextMuted, fontSize = 10.sp)
                                        }
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(conv.lastMessage, color = TextSecondary, fontSize = 12.sp, maxLines = 1)
                                    }

                                    if (conv.unreadCount > 0) {
                                        Surface(shape = CircleShape, color = NeonMagenta) {
                                            Text(
                                                "${conv.unreadCount}",
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
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
    }

    // Global Comments Bottom Sheet
    if (activeCommentsVideoId != null) {
        val comments = commentsMap[activeCommentsVideoId] ?: emptyList()
        CommentsBottomSheet(
            comments = comments,
            stickers = viewModel.availableStickers,
            onDismiss = { viewModel.closeComments() },
            onSendComment = { text, sticker ->
                viewModel.repository.addComment(activeCommentsVideoId!!, text, sticker)
            }
        )
    }

    // Global Virtual Gifts Bottom Sheet
    if (isGiftSheetOpen) {
        VirtualGiftsBottomSheet(
            gifts = viewModel.virtualGifts,
            userCoins = currentUser.coinsBalance,
            onDismiss = { viewModel.closeGiftSheet() },
            onSendGift = { gift ->
                viewModel.repository.sendLiveGift(gift)
                viewModel.closeGiftSheet()
            }
        )
    }

    // Global OAuth Sign-In Modal
    if (isAuthModalOpen) {
        AuthOAuthDialog(
            supportedCountries = viewModel.supportedCountries,
            onDismiss = { viewModel.isAuthModalOpen.value = false },
            onLoginSuccess = { provider ->
                viewModel.simulateOAuthLogin(provider)
            }
        )
    }

    // Global Language Selector Modal
    if (isLangModalOpen) {
        LanguageSelectorDialog(
            currentLanguage = currentLanguage,
            onSelectLanguage = { viewModel.repository.setLanguage(it) },
            onDismiss = { viewModel.isLanguageModalOpen.value = false }
        )
    }
}
