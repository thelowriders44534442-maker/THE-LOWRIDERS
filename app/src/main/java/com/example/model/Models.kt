package com.example.model

enum class VideoType {
    SHORT_PULSE,
    STANDARD_LONG
}

data class UserBadge(
    val id: String,
    val name: String,
    val iconEmoji: String,
    val description: String,
    val colorHex: Long = 0xFF00F0FF
)

data class SocialLink(
    val platform: String, // Twitch, YouTube, X, Discord, Steam, TikTok
    val username: String,
    val url: String,
    val iconEmoji: String
)

enum class ProfileWidgetType {
    PINNED_HIGHLIGHTS,
    HARDWARE_SPECS,
    BADGES_SHOWCASE,
    COMMUNITY_HUBS,
    CREATOR_STATS
}

data class ProfileWidget(
    val type: ProfileWidgetType,
    val title: String,
    val isEnabled: Boolean = true,
    val order: Int = 0
)

data class HardwareSpec(
    val component: String,
    val spec: String,
    val iconEmoji: String
)

data class UserProfile(
    val id: String,
    val username: String,
    val handle: String,
    val displayName: String,
    val bio: String,
    val pronouns: String = "they/them",
    val gamingStatus: String = "Playing Cyberpunk 2077: Phantom Liberty",
    val bannerGradientIndex: Int = 0,
    val avatarEmoji: String = "⚡",
    val avatarBorderColor: Long = 0xFF00F0FF,
    val level: Int = 42,
    val xp: Int = 8450,
    val nextLevelXp: Int = 10000,
    val reputationScore: Int = 99, // percentage
    val rankTitle: String = "Neon Vanguard",
    val followersCount: Int = 142800,
    val followingCount: Int = 420,
    val totalLikesCount: String = "2.8M",
    val coinsBalance: Int = 2850,
    val badges: List<UserBadge> = emptyList(),
    val socialLinks: List<SocialLink> = emptyList(),
    val hardwareSpecs: List<HardwareSpec> = emptyList(),
    val activeWidgets: List<ProfileWidget> = emptyList(),
    val pinnedVideoIds: List<String> = emptyList()
)

data class Comment(
    val id: String,
    val videoId: String,
    val authorName: String,
    val authorHandle: String,
    val authorAvatar: String,
    val text: String,
    val stickerEmoji: String? = null,
    val likesCount: Int = 12,
    val isLiked: Boolean = false,
    val timeAgo: String = "2h ago",
    val authorBadge: String? = null
)

data class VideoItem(
    val id: String,
    val title: String,
    val description: String,
    val creatorId: String,
    val creatorName: String,
    val creatorHandle: String,
    val creatorAvatar: String,
    val creatorBadge: String = "Verified",
    val type: VideoType,
    val gameCategory: String,
    val tags: List<String>,
    val viewsText: String,
    val likesCount: Int,
    val isLiked: Boolean = false,
    val commentsCount: Int,
    val sharesCount: Int,
    val giftsCount: Int = 0,
    val isBookmarked: Boolean = false,
    val isFollowed: Boolean = false,
    val uploadTimeAgo: String,
    val durationText: String,
    val soundTrackTitle: String = "Original Sound - PlayPulse Beats",
    val gradientColors: List<Long>
)

data class VirtualGift(
    val id: String,
    val name: String,
    val emoji: String,
    val coinCost: Int,
    val animationType: String,
    val highlightColor: Long
)

data class GiftEvent(
    val senderName: String,
    val gift: VirtualGift,
    val comboCount: Int = 1,
    val timestampMs: Long = System.currentTimeMillis()
)

data class LiveChatMessage(
    val id: String,
    val senderName: String,
    val senderBadge: String? = null,
    val message: String,
    val isHighlighted: Boolean = false,
    val tipAmountCoins: Int? = null
)

data class LiveStream(
    val id: String,
    val streamerName: String,
    val streamerHandle: String,
    val streamerAvatar: String,
    val title: String,
    val gameCategory: String,
    val viewersCount: Int,
    val totalGiftsSent: Int,
    val streamTags: List<String>,
    val isLive: Boolean = true,
    val recentGifts: List<GiftEvent> = emptyList(),
    val chatMessages: List<LiveChatMessage> = emptyList()
)

enum class ChannelType {
    TEXT,
    VOICE,
    ANNOUNCEMENT,
    CLIPS_ONLY
}

data class VoiceParticipant(
    val id: String,
    val name: String,
    val avatarEmoji: String,
    val isSpeaking: Boolean = false,
    val isMuted: Boolean = false,
    val isDeafened: Boolean = false,
    val roleName: String = "Squad Member"
)

data class HubChannel(
    val id: String,
    val name: String,
    val type: ChannelType,
    val topic: String,
    val unreadMessagesCount: Int = 0,
    val activeVoiceUsers: List<VoiceParticipant> = emptyList()
)

data class HubCategory(
    val name: String,
    val channels: List<HubChannel>
)

data class CommunityHub(
    val id: String,
    val name: String,
    val tag: String,
    val iconEmoji: String,
    val bannerGradient: List<Long>,
    val description: String,
    val memberCount: Int,
    val onlineCount: Int,
    val isJoined: Boolean = false,
    val verifiedPillar: Boolean = true,
    val categories: List<HubCategory> = emptyList(),
    val rules: List<String> = emptyList()
)

data class ChatMessage(
    val id: String,
    val senderId: String,
    val senderName: String,
    val text: String,
    val stickerEmoji: String? = null,
    val timestamp: String,
    val isMe: Boolean,
    val isEncrypted: Boolean = true
)

data class DmConversation(
    val id: String,
    val peerUser: UserProfile,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int = 0,
    val isOnline: Boolean = true,
    val messages: List<ChatMessage> = emptyList()
)

data class ShopItem(
    val id: String,
    val name: String,
    val category: String, // "GEAR", "GIFTS", "SKINS", "AVATAR_AURA"
    val priceCoins: Int,
    val priceUsd: Double,
    val emoji: String,
    val description: String,
    val rating: Float = 4.9f,
    val purchasesCount: Int = 1204
)

data class ReputationQuest(
    val id: String,
    val title: String,
    val description: String,
    val xpReward: Int,
    val coinReward: Int,
    val currentProgress: Int,
    val targetProgress: Int,
    val isCompleted: Boolean = false,
    val isClaimed: Boolean = false,
    val iconEmoji: String = "🏆"
)

data class CreatorAnalytics(
    val totalViews: String = "1.42M",
    val viewsGrowth: String = "+28.4%",
    val watchTimeHours: String = "84.2K hrs",
    val estimatedEarnings: Double = 3480.50,
    val newFollowers: String = "+14.6K",
    val engagementRate: String = "9.4%",
    val topVideoTitle: String = "How I Hit Radiant in Valorant with Solo Q ONLY",
    val topVideoViews: String = "482K views",
    val audienceRegions: List<Pair<String, Int>> = listOf(
        "North America" to 44,
        "Europe" to 26,
        "Asia-Pacific" to 20,
        "Latin America" to 10
    )
)

data class Sticker(
    val id: String,
    val name: String,
    val emoji: String,
    val category: String
)

data class CountryInfo(
    val name: String,
    val code: String,
    val dialCode: String,
    val flagEmoji: String
)
