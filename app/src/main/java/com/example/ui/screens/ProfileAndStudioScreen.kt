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
fun ProfileAndStudioScreen(
    viewModel: PlayPulseViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val analytics by viewModel.creatorAnalytics.collectAsState()
    val quests by viewModel.reputationQuests.collectAsState()
    val shopItems by viewModel.shopItems.collectAsState()
    val isEditOpen by viewModel.isEditProfileOpen.collectAsState()
    val isUploadOpen by viewModel.isCreateUploadOpen.collectAsState()

    var activeSubTab by remember { mutableIntStateOf(0) } // 0: Profile, 1: Creator Studio, 2: Gaming Shop
    var shopPurchaseMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkVoid)
            .testTag("profile_and_studio_screen")
    ) {
        // Top Segmented Control (Profile / Studio / Shop)
        Surface(
            color = DarkSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TabRow(
                    selectedTabIndex = activeSubTab,
                    containerColor = Color.Transparent,
                    contentColor = NeonCyan,
                    modifier = Modifier.weight(1f)
                ) {
                    Tab(
                        selected = activeSubTab == 0,
                        onClick = { activeSubTab = 0 },
                        text = { Text("Discord Profile", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                    )
                    Tab(
                        selected = activeSubTab == 1,
                        onClick = { activeSubTab = 1 },
                        text = { Text("Studio & Analytics", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                    )
                    Tab(
                        selected = activeSubTab == 2,
                        onClick = { activeSubTab = 2 },
                        text = { Text("Gaming Shop", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                    )
                }

                IconButton(
                    onClick = { viewModel.toggleDarkMode() },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (viewModel.isDarkMode.collectAsState().value) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = "Theme",
                        tint = NeonYellow
                    )
                }
            }
        }

        // Sub Tab Content
        when (activeSubTab) {
            0 -> ProfileCustomizationTab(
                user = currentUser,
                viewModel = viewModel,
                onEditClick = { viewModel.isEditProfileOpen.value = true }
            )
            1 -> CreatorStudioTab(
                analytics = analytics,
                quests = quests,
                viewModel = viewModel,
                onUploadClick = { viewModel.isCreateUploadOpen.value = true }
            )
            2 -> GamingShopTab(
                shopItems = shopItems,
                userCoins = currentUser.coinsBalance,
                onBuyItem = { item ->
                    val success = viewModel.repository.buyShopItem(item)
                    shopPurchaseMessage = if (success) "Successfully obtained ${item.name}!" else "Not enough coins! Refill in shop."
                },
                toastMessage = shopPurchaseMessage,
                onClearToast = { shopPurchaseMessage = null }
            )
        }
    }

    // Edit Profile & Layout Widgets Modal
    if (isEditOpen) {
        EditProfileModal(
            currentUser = currentUser,
            onDismiss = { viewModel.isEditProfileOpen.value = false },
            onSave = { name, bio, pronouns, status, bannerIdx, emoji, borderColor, widgets ->
                viewModel.repository.updateProfileCustomization(
                    displayName = name,
                    bio = bio,
                    pronouns = pronouns,
                    gamingStatus = status,
                    bannerIndex = bannerIdx,
                    avatarEmoji = emoji,
                    borderColor = borderColor,
                    widgets = widgets
                )
                viewModel.isEditProfileOpen.value = false
            }
        )
    }

    // Creator Simple Upload Modal
    if (isUploadOpen) {
        UploadVideoModal(
            onDismiss = { viewModel.isCreateUploadOpen.value = false },
            onUpload = { title, desc, cat, tags, isShort ->
                viewModel.repository.uploadVideo(title, desc, cat, tags, isShort)
                viewModel.isCreateUploadOpen.value = false
            }
        )
    }
}

@Composable
fun ProfileCustomizationTab(
    user: UserProfile,
    viewModel: PlayPulseViewModel,
    onEditClick: () -> Unit
) {
    val bannerGradients = listOf(
        listOf(0xFF0F2027, 0xFF203A43, 0xFF2C5364),
        listOf(0xFF8A2387, 0xFFE94057, 0xFFF27121),
        listOf(0xFF000428, 0xFF004E92),
        listOf(0xFF1F1C2C, 0xFF928DAB)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 72.dp)
    ) {
        // Discord-Style Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(
                        Brush.horizontalGradient(
                            bannerGradients.getOrElse(user.bannerGradientIndex) { bannerGradients[0] }.map { Color(it) }
                        )
                    )
            ) {
                // Edit button in top right of banner
                Button(
                    onClick = onEditClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black.copy(alpha = 0.6f)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .testTag("edit_profile_button")
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(14.dp), tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Customize", fontSize = 11.sp, color = Color.White)
                }
            }
        }

        // Avatar, Name, Handle, Pronouns, and Status
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .offset(y = (-40).dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AvatarWithNeonRing(
                        emoji = user.avatarEmoji,
                        size = 80.dp,
                        ringColor = Color(user.avatarBorderColor),
                        isOnline = true
                    )

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = DarkSurfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonYellow.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("🪙", fontSize = 13.sp)
                            Text("${user.coinsBalance} Coins", color = NeonYellow, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(user.displayName, color = TextPrimary, fontWeight = FontWeight.Black, fontSize = 18.sp)
                    NeonBadge(text = user.pronouns, borderColor = NeonCyan)
                }

                Text(user.handle, color = TextMuted, fontSize = 12.sp)

                Spacer(modifier = Modifier.height(6.dp))

                // Gaming Status
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = DarkSurfaceVariant
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("🎮", fontSize = 12.sp)
                        Text(user.gamingStatus, color = NeonGreen, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(user.bio, color = TextPrimary.copy(alpha = 0.9f), fontSize = 13.sp, lineHeight = 18.sp)

                Spacer(modifier = Modifier.height(14.dp))

                // Level & Gamified Reputation Bar
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DarkSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Level ${user.level} • ${user.rankTitle}", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("${user.reputationScore}% Good Karma", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { user.xp.toFloat() / user.nextLevelXp },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = NeonCyan,
                            trackColor = DarkBorder
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("${user.xp} / ${user.nextLevelXp} XP to Level ${user.level + 1}", color = TextMuted, fontSize = 10.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Social Links with App Icons Integration (Discord style)
                Text("Connected Socials", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(user.socialLinks) { link ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = DarkSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(link.iconEmoji, fontSize = 13.sp)
                                Text(link.platform, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Customization Widgets (Ordered by user settings)
                user.activeWidgets.filter { it.isEnabled }.forEach { widget ->
                    when (widget.type) {
                        ProfileWidgetType.PINNED_HIGHLIGHTS -> {
                            ProfileWidgetSection(title = widget.title) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = DarkSurfaceVariant,
                                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f)),
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(90.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(10.dp),
                                            verticalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text("📌 Cyberpunk 2077 Next-Gen Mod Guide", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 2)
                                            Text("👁️ 842K views", color = NeonCyan, fontSize = 10.sp)
                                        }
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = DarkSurfaceVariant,
                                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonMagenta.copy(alpha = 0.5f)),
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(90.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(10.dp),
                                            verticalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text("📌 Radiant Sheriff Ace Clutch", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 2)
                                            Text("👁️ 3.8M views", color = NeonMagenta, fontSize = 10.sp)
                                        }
                                    }
                                }
                            }
                        }

                        ProfileWidgetType.HARDWARE_SPECS -> {
                            ProfileWidgetSection(title = widget.title) {
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    user.hardwareSpecs.forEach { spec ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text("${spec.iconEmoji} ${spec.component}", color = TextSecondary, fontSize = 12.sp)
                                            Text(spec.spec, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        }
                                        HorizontalDivider(color = DarkBorder.copy(alpha = 0.4f))
                                    }
                                }
                            }
                        }

                        ProfileWidgetType.BADGES_SHOWCASE -> {
                            ProfileWidgetSection(title = widget.title) {
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    items(user.badges) { badge ->
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = DarkSurfaceVariant,
                                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(badge.colorHex))
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Text(badge.iconEmoji, fontSize = 16.sp)
                                                Column {
                                                    Text(badge.name, color = Color(badge.colorHex), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                                    Text(badge.description, color = TextMuted, fontSize = 9.sp)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        ProfileWidgetType.COMMUNITY_HUBS -> {
                            ProfileWidgetSection(title = widget.title) {
                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    NeonBadge(text = "🎯 Apex Syndicate", borderColor = NeonMagenta)
                                    NeonBadge(text = "🌆 Night City Modders", borderColor = NeonCyan)
                                    NeonBadge(text = "⏱️ Speedrunners", borderColor = NeonYellow)
                                }
                            }
                        }

                        ProfileWidgetType.CREATOR_STATS -> {
                            ProfileWidgetSection(title = widget.title) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("${user.followersCount / 1000}K", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                        Text("Followers", color = TextMuted, fontSize = 11.sp)
                                    }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(user.totalLikesCount, color = NeonMagenta, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                        Text("Likes", color = TextMuted, fontSize = 11.sp)
                                    }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("${user.followingCount}", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                        Text("Following", color = TextMuted, fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun ProfileWidgetSection(
    title: String,
    content: @Composable () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = DarkSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(title, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
fun CreatorStudioTab(
    analytics: CreatorAnalytics,
    quests: List<ReputationQuest>,
    viewModel: PlayPulseViewModel,
    onUploadClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .padding(bottom = 72.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Upload Action Bar
        item {
            Button(
                onClick = onUploadClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("upload_content_button"),
                colors = ButtonDefaults.buttonColors(containerColor = NeonMagenta),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Upload, contentDescription = "Upload")
                Spacer(modifier = Modifier.width(6.dp))
                Text("Upload Video or Pulse Short", fontWeight = FontWeight.Bold)
            }
        }

        // Deep Analytics KPI Grid
        item {
            Text("Deep Creator Analytics (Last 28 Days)", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AnalyticsCard(title = "Total Views", value = analytics.totalViews, change = analytics.viewsGrowth, modifier = Modifier.weight(1f))
                AnalyticsCard(title = "Watch Time", value = analytics.watchTimeHours, change = "+19.2%", modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AnalyticsCard(title = "Estimated Earnings", value = "$${analytics.estimatedEarnings}", change = "+34.5%", isPositive = true, modifier = Modifier.weight(1f))
                AnalyticsCard(title = "Audience Growth", value = analytics.newFollowers, change = "+12.8%", modifier = Modifier.weight(1f))
            }
        }

        // Audience Demographics breakdown
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = DarkSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Top Audience Geographic Reach", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    analytics.audienceRegions.forEach { (region, pct) ->
                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(region, color = TextSecondary, fontSize = 12.sp)
                                Text("$pct%", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            LinearProgressIndicator(
                                progress = { pct / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp)),
                                color = NeonCyan,
                                trackColor = DarkBorder
                            )
                        }
                    }
                }
            }
        }

        // Gamified Reputation Quests
        item {
            Text("Daily Quests & Creator XP", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                quests.forEach { quest ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = DarkSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (quest.isCompleted && !quest.isClaimed) NeonGreen else DarkBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(quest.iconEmoji, fontSize = 24.sp)
                                Column {
                                    Text(quest.title, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(quest.description, color = TextMuted, fontSize = 11.sp)
                                    Text("+${quest.xpReward} XP • +${quest.coinReward} Coins", color = NeonYellow, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            if (quest.isClaimed) {
                                Text("Claimed ✓", color = TextMuted, fontSize = 11.sp)
                            } else if (quest.isCompleted) {
                                Button(
                                    onClick = { viewModel.repository.claimQuest(quest.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Text("Claim", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                            } else {
                                Text("${quest.currentProgress}/${quest.targetProgress}", color = TextMuted, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnalyticsCard(
    title: String,
    value: String,
    change: String,
    isPositive: Boolean = true,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = DarkSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, color = TextSecondary, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(change, color = if (isPositive) NeonGreen else Color.Red, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun GamingShopTab(
    shopItems: List<ShopItem>,
    userCoins: Int,
    onBuyItem: (ShopItem) -> Unit,
    toastMessage: String?,
    onClearToast: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .padding(bottom = 72.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Wallet Card
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = DarkSurface,
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Brush.horizontalGradient(listOf(NeonYellow, NeonOrange))),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Pulse Coin Wallet", color = TextSecondary, fontSize = 12.sp)
                        Text("$userCoins Coins", color = NeonYellow, fontWeight = FontWeight.Black, fontSize = 24.sp)
                        Text("Instant in-app gift & creator perks", color = TextMuted, fontSize = 11.sp)
                    }

                    Button(
                        onClick = {
                            onBuyItem(
                                ShopItem("topup", "Instant Coin Top-Up", "COINS", 1000, 9.99, "🪙", "Instant refill")
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonYellow),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("+ Top Up", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        if (toastMessage != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = NeonGreen.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(toastMessage, color = Color.White, fontSize = 12.sp)
                        IconButton(onClick = onClearToast, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        // Shop Items Grid
        items(shopItems) { item ->
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = DarkSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = DarkSurfaceVariant,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(item.emoji, fontSize = 30.sp)
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(item.name, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(item.description, color = TextSecondary, fontSize = 11.sp, maxLines = 2)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("🪙 ${item.priceCoins} Coins", color = NeonYellow, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("• $${item.priceUsd}", color = TextMuted, fontSize = 11.sp)
                        }
                    }

                    Button(
                        onClick = { onBuyItem(item) },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Buy", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun EditProfileModal(
    currentUser: UserProfile,
    onDismiss: () -> Unit,
    onSave: (displayName: String, bio: String, pronouns: String, status: String, bannerIdx: Int, emoji: String, borderColor: Long, widgets: List<ProfileWidget>) -> Unit
) {
    var displayName by remember { mutableStateOf(currentUser.displayName) }
    var bio by remember { mutableStateOf(currentUser.bio) }
    var pronouns by remember { mutableStateOf(currentUser.pronouns) }
    var gamingStatus by remember { mutableStateOf(currentUser.gamingStatus) }
    var selectedBanner by remember { mutableIntStateOf(currentUser.bannerGradientIndex) }
    var selectedEmoji by remember { mutableStateOf(currentUser.avatarEmoji) }
    var selectedBorderColor by remember { mutableLongStateOf(currentUser.avatarBorderColor) }
    var widgets by remember { mutableStateOf(currentUser.activeWidgets) }

    val emojis = listOf("👾", "⚡", "👑", "🗡️", "🏎️", "🎧", "🐉", "🔥")
    val borderColors = listOf(0xFF00F0FF, 0xFFFF007F, 0xFFFFD700, 0xFF00FFA3, 0xFF8A2BE2)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 580.dp)
                .clip(RoundedCornerShape(20.dp)),
            color = DarkSurface
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Discord Profile Customization", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                LazyColumn(modifier = Modifier.weight(1f)) {
                    item {
                        Text("Display Name", color = TextSecondary, fontSize = 12.sp)
                        OutlinedTextField(
                            value = displayName,
                            onValueChange = { displayName = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Gaming Activity Status", color = TextSecondary, fontSize = 12.sp)
                        OutlinedTextField(
                            value = gamingStatus,
                            onValueChange = { gamingStatus = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Pronouns", color = TextSecondary, fontSize = 12.sp)
                        OutlinedTextField(
                            value = pronouns,
                            onValueChange = { pronouns = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Bio", color = TextSecondary, fontSize = 12.sp)
                        OutlinedTextField(
                            value = bio,
                            onValueChange = { bio = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        Text("Avatar Cyber Symbol", color = TextSecondary, fontSize = 12.sp)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(emojis) { em ->
                                Surface(
                                    shape = CircleShape,
                                    color = if (selectedEmoji == em) NeonCyan.copy(alpha = 0.3f) else DarkSurfaceVariant,
                                    modifier = Modifier
                                        .size(42.dp)
                                        .border(
                                            1.dp,
                                            if (selectedEmoji == em) NeonCyan else Color.Transparent,
                                            CircleShape
                                        )
                                        .clickable { selectedEmoji = em }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(em, fontSize = 20.sp)
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))

                        Text("Neon Ring Accent Color", color = TextSecondary, fontSize = 12.sp)
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            borderColors.forEach { col ->
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(Color(col))
                                        .border(
                                            2.dp,
                                            if (selectedBorderColor == col) Color.White else Color.Transparent,
                                            CircleShape
                                        )
                                        .clickable { selectedBorderColor = col }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))

                        Text("Banner Theme", color = TextSecondary, fontSize = 12.sp)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            (0..3).forEach { idx ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = DarkSurfaceVariant,
                                    modifier = Modifier
                                        .size(width = 60.dp, height = 36.dp)
                                        .border(
                                            2.dp,
                                            if (selectedBanner == idx) NeonCyan else Color.Transparent,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .clickable { selectedBanner = idx }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text("Style ${idx + 1}", fontSize = 10.sp, color = Color.White)
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))

                        Text("Profile Widgets Layout", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        widgets.forEachIndexed { index, w ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(w.title, color = TextPrimary, fontSize = 12.sp)
                                Switch(
                                    checked = w.isEnabled,
                                    onCheckedChange = { isChecked ->
                                        widgets = widgets.toMutableList().also { list ->
                                            list[index] = w.copy(isEnabled = isChecked)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        onSave(displayName, bio, pronouns, gamingStatus, selectedBanner, selectedEmoji, selectedBorderColor, widgets)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                ) {
                    Text("Save Customizations", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun UploadVideoModal(
    onDismiss: () -> Unit,
    onUpload: (title: String, desc: String, category: String, tags: List<String>, isShort: Boolean) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Apex Legends") }
    var isShort by remember { mutableStateOf(true) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp)),
            color = DarkSurface
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Creator Studio Upload", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    FilterChip(
                        selected = isShort,
                        onClick = { isShort = true },
                        label = { Text("Pulse Short") }
                    )
                    FilterChip(
                        selected = !isShort,
                        onClick = { isShort = false },
                        label = { Text("Standard Video") }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description & Tags") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Game Category") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            onUpload(title, description, category, listOf("Gaming", category), isShort)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonMagenta)
                ) {
                    Text("Publish to PlayPulse", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
