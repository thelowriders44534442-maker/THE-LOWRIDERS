package com.example.ui.screens

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.VideoItem
import com.example.ui.PlayPulseViewModel
import com.example.ui.components.AvatarWithNeonRing
import com.example.ui.components.NeonBadge
import com.example.ui.theme.*

@Composable
fun FeedScreen(
    viewModel: PlayPulseViewModel,
    modifier: Modifier = Modifier
) {
    val longVideos by viewModel.longVideos.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val activeWatchVideo by viewModel.activeWatchVideo.collectAsState()

    val categories = listOf(
        "All Games", "Apex Legends", "Cyberpunk 2077", "Elden Ring",
        "Valorant", "Esports", "Tech & Gear", "Speedruns"
    )

    val filteredVideos = remember(longVideos, selectedCategory, searchQuery) {
        longVideos.filter { video ->
            val matchCategory = selectedCategory == "All Games" || video.gameCategory.equals(selectedCategory, ignoreCase = true)
            val matchSearch = searchQuery.isBlank() ||
                    video.title.contains(searchQuery, ignoreCase = true) ||
                    video.tags.any { it.contains(searchQuery, ignoreCase = true) }
            matchCategory && matchSearch
        }
    }

    if (activeWatchVideo != null) {
        // Detailed Watch Player Screen (YouTube style)
        WatchPlayerView(
            video = activeWatchVideo!!,
            viewModel = viewModel,
            onClose = { viewModel.selectWatchVideo(null) }
        )
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(DarkVoid)
                .testTag("feed_screen")
        ) {
            // Top Search Bar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(DarkSurfaceVariant)
                        .border(1.dp, DarkBorder, RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = NeonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setSearchQuery(it) },
                        placeholder = {
                            Text(
                                "Search videos, esports, guides, tags...",
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
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear", tint = TextMuted)
                        }
                    }
                }
            }

            // Category Chips Row
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categories) { category ->
                        val isSelected = category == selectedCategory
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setSelectedCategory(category) },
                            label = {
                                Text(
                                    text = category,
                                    color = if (isSelected) Color.White else TextSecondary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.sp
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NeonCyan.copy(alpha = 0.25f),
                                containerColor = DarkSurfaceVariant
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                selectedBorderColor = NeonCyan,
                                borderColor = DarkBorder
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                }
            }

            // Trending Banner
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, Brush.horizontalGradient(listOf(NeonCyan, NeonMagenta)), RoundedCornerShape(16.dp)),
                    color = DarkSurface
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                NeonBadge(text = "COMMUNITY HIGHLIGHT", borderColor = NeonYellow)
                                Text("🔥 1.2M LIVE", color = NeonMagenta, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Apex Legends World Championship & Cyberpunk 2077 Night City Mod Showcase",
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        IconButton(
                            onClick = { viewModel.setSelectedCategory("Apex Legends") },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(NeonCyan.copy(alpha = 0.2f))
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Watch", tint = NeonCyan)
                        }
                    }
                }
            }

            // Video Cards List (YouTube Style)
            items(filteredVideos, key = { it.id }) { video ->
                VideoCardItem(
                    video = video,
                    onClick = { viewModel.selectWatchVideo(video) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(72.dp))
            }
        }
    }
}

@Composable
fun VideoCardItem(
    video: VideoItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(bottom = 18.dp)
            .testTag("video_card_${video.id}")
    ) {
        // Thumbnail Box with Gradient & Duration
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    Brush.verticalGradient(
                        video.gradientColors.map { Color(it) }
                    )
                )
        ) {
            // Neon Gaming Visual Elements
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.5f),
                    modifier = Modifier.size(54.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play Video",
                            tint = NeonCyan,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }

            // Game Category Tag top-left
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color.Black.copy(alpha = 0.7f),
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.TopStart)
            ) {
                Text(
                    text = video.gameCategory,
                    color = NeonCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            // Duration badge bottom-right
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color.Black.copy(alpha = 0.85f),
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Text(
                    text = video.durationText,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        // Video Info Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AvatarWithNeonRing(
                emoji = video.creatorAvatar,
                size = 40.dp,
                ringColor = NeonCyan
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = video.title,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 19.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = video.creatorName,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text("•", color = TextMuted, fontSize = 10.sp)
                    Text(
                        text = video.viewsText,
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                    Text("•", color = TextMuted, fontSize = 10.sp)
                    Text(
                        text = video.uploadTimeAgo,
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun WatchPlayerView(
    video: VideoItem,
    viewModel: PlayPulseViewModel,
    onClose: () -> Unit
) {
    var isSubscribed by remember { mutableStateOf(video.isFollowed) }
    val commentsMap by viewModel.commentsMap.collectAsState()
    val comments = commentsMap[video.id] ?: emptyList()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkVoid)
            .testTag("watch_player_view")
    ) {
        // Top Player Screen
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .background(
                        Brush.verticalGradient(
                            video.gradientColors.map { Color(it) }
                        )
                    )
            ) {
                // Close player button
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopStart)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.6f))
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }

                // Center simulated play controls
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.PauseCircle,
                        contentDescription = "Playing",
                        tint = NeonCyan,
                        modifier = Modifier.size(56.dp)
                    )
                    Text(
                        text = "1080p 60fps • Ultra Bitrate",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }

                // Progress Bar
                LinearProgressIndicator(
                    progress = { 0.35f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .height(3.dp),
                    color = NeonCyan,
                    trackColor = DarkBorder
                )
            }
        }

        // Title & Stats
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = video.title,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = video.viewsText, color = TextMuted, fontSize = 12.sp)
                    Text("•", color = TextMuted, fontSize = 10.sp)
                    Text(text = video.uploadTimeAgo, color = TextMuted, fontSize = 12.sp)
                    NeonBadge(text = video.gameCategory, borderColor = NeonCyan)
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Creator Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        AvatarWithNeonRing(
                            emoji = video.creatorAvatar,
                            size = 42.dp,
                            ringColor = NeonCyan
                        )
                        Column {
                            Text(video.creatorName, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(video.creatorBadge, color = NeonCyan, fontSize = 11.sp)
                        }
                    }

                    Button(
                        onClick = {
                            isSubscribed = !isSubscribed
                            viewModel.repository.toggleFollowCreator(video.creatorId)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSubscribed) DarkSurfaceVariant else NeonMagenta
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.testTag("subscribe_button")
                    ) {
                        Text(
                            text = if (isSubscribed) "Joined" else "Subscribe",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Action buttons: Like, Comment, Share, Gift
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    PlayerActionButton(
                        icon = if (video.isLiked) Icons.Default.ThumbUp else Icons.Default.ThumbUpOffAlt,
                        label = "${video.likesCount}",
                        tint = if (video.isLiked) NeonCyan else TextPrimary,
                        onClick = { viewModel.repository.toggleLikeVideo(video.id, isShort = false) }
                    )
                    PlayerActionButton(
                        icon = Icons.Default.ChatBubbleOutline,
                        label = "${comments.size}",
                        tint = TextPrimary,
                        onClick = { viewModel.openComments(video.id) }
                    )
                    PlayerActionButton(
                        icon = Icons.Default.CardGiftcard,
                        label = "Send Gift",
                        tint = NeonYellow,
                        onClick = { viewModel.openGiftSheet() }
                    )
                    PlayerActionButton(
                        icon = Icons.Default.Share,
                        label = "Share",
                        tint = TextPrimary,
                        onClick = { }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Description box
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DarkSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Description", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = video.description,
                            color = TextPrimary.copy(alpha = 0.9f),
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Comments Preview section
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurfaceVariant)
                        .clickable { viewModel.openComments(video.id) }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Comments • ${comments.size}", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        if (comments.isNotEmpty()) {
                            Text(
                                text = "${comments.first().authorName}: ${comments.first().text}",
                                color = TextSecondary,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        } else {
                            Text("Be the first to comment", color = TextMuted, fontSize = 12.sp)
                        }
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = "Open Comments", tint = TextMuted)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun PlayerActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    tint: Color,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = DarkSurfaceVariant,
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(imageVector = icon, contentDescription = label, tint = tint, modifier = Modifier.size(18.dp))
            Text(text = label, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
