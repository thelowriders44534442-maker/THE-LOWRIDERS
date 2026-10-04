package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.VideoItem
import com.example.ui.PlayPulseViewModel
import com.example.ui.components.AvatarWithNeonRing
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun PulseShortsScreen(
    viewModel: PlayPulseViewModel,
    modifier: Modifier = Modifier
) {
    val shortVideos by viewModel.shortVideos.collectAsState()
    val pagerState = rememberPagerState(pageCount = { shortVideos.size })

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkVoid)
            .testTag("pulse_shorts_screen")
    ) {
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val video = shortVideos[page]
            ShortVideoItemView(
                video = video,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun ShortVideoItemView(
    video: VideoItem,
    viewModel: PlayPulseViewModel
) {
    var showHeartAnimation by remember { mutableStateOf(false) }
    var isBookmarked by remember { mutableStateOf(video.isBookmarked) }

    // Spinning disc animation for music
    val infiniteTransition = rememberInfiniteTransition(label = "music_disc")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    video.gradientColors.map { Color(it) }
                )
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        if (!video.isLiked) {
                            viewModel.repository.toggleLikeVideo(video.id, isShort = true)
                        }
                        showHeartAnimation = true
                    }
                )
            }
    ) {
        // Center Simulated Gameplay Graphics & Soundwave
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                color = Color.Black.copy(alpha = 0.35f),
                modifier = Modifier.size(72.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Playing Pulse Clip",
                        tint = NeonCyan,
                        modifier = Modifier.size(44.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.Black.copy(alpha = 0.6f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("🎮 ${video.gameCategory}", color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Heart Burst Animation on Double Tap
        LaunchedEffect(showHeartAnimation) {
            if (showHeartAnimation) {
                delay(800)
                showHeartAnimation = false
            }
        }

        AnimatedVisibility(
            visible = showHeartAnimation,
            enter = scaleIn(spring(dampingRatio = Spring.DampingRatioMediumBouncy)) + fadeIn(),
            exit = scaleOut() + fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Heart Pop",
                tint = NeonMagenta,
                modifier = Modifier.size(100.dp)
            )
        }

        // Right Action Rail (TikTok Style)
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Creator Avatar with Follow button
            Box(contentAlignment = Alignment.BottomCenter) {
                AvatarWithNeonRing(
                    emoji = video.creatorAvatar,
                    size = 46.dp,
                    ringColor = NeonCyan
                )
                if (!video.isFollowed) {
                    Box(
                        modifier = Modifier
                            .offset(y = 8.dp)
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(NeonMagenta)
                            .clickable { viewModel.repository.toggleFollowCreator(video.creatorId) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Follow",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Like Action
            ShortActionButton(
                icon = if (video.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                label = formatCount(video.likesCount),
                tint = if (video.isLiked) NeonMagenta else Color.White,
                onClick = { viewModel.repository.toggleLikeVideo(video.id, isShort = true) },
                testTag = "short_like_button"
            )

            // Comments Action
            ShortActionButton(
                icon = Icons.Default.ChatBubble,
                label = formatCount(video.commentsCount),
                tint = Color.White,
                onClick = { viewModel.openComments(video.id) },
                testTag = "short_comment_button"
            )

            // Virtual Gifts Action (TikTok Live/Shorts style)
            ShortActionButton(
                icon = Icons.Default.CardGiftcard,
                label = "${video.giftsCount}",
                tint = NeonYellow,
                onClick = { viewModel.openGiftSheet() },
                testTag = "short_gift_button"
            )

            // Bookmark Action
            ShortActionButton(
                icon = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                label = "Save",
                tint = if (isBookmarked) NeonCyan else Color.White,
                onClick = { isBookmarked = !isBookmarked }
            )

            // Share Action
            ShortActionButton(
                icon = Icons.Default.Share,
                label = formatCount(video.sharesCount),
                tint = Color.White,
                onClick = { }
            )

            // Rotating Music Disc
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .border(2.dp, NeonCyan, CircleShape)
                    .rotate(rotation),
                contentAlignment = Alignment.Center
            ) {
                Text("🎵", fontSize = 18.sp)
            }
        }

        // Bottom Details (Creator Name, Title, Sound)
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(0.78f)
                .padding(start = 16.dp, bottom = 80.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = video.creatorName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = NeonCyan.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan)
                ) {
                    Text(
                        text = video.creatorBadge,
                        color = NeonCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = video.title,
                color = Color.White.copy(alpha = 0.95f),
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = video.description,
                color = Color.White.copy(alpha = 0.75f),
                fontSize = 12.sp,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Sound Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = "Sound",
                    tint = NeonGreen,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = video.soundTrackTitle,
                    color = Color.White,
                    fontSize = 11.sp,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun ShortActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    tint: Color,
    onClick: () -> Unit,
    testTag: String = ""
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .then(if (testTag.isNotEmpty()) Modifier.testTag(testTag) else Modifier)
    ) {
        Surface(
            shape = CircleShape,
            color = Color.Black.copy(alpha = 0.45f),
            modifier = Modifier.size(44.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = tint,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

fun formatCount(count: Int): String {
    return when {
        count >= 1_000_000 -> "${count / 1_000_000}.${(count % 1_000_000) / 100_000}M"
        count >= 1_000 -> "${count / 1_000}.${(count % 1_000) / 100}K"
        else -> count.toString()
    }
}
