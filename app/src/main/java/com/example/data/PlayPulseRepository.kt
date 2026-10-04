package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PlayPulseRepository {

    // Current User Profile with Discord-style full customization
    private val _currentUser = MutableStateFlow(
        UserProfile(
            id = "user_me",
            username = "NeonPhantom",
            handle = "@neon_phantom",
            displayName = "Kaelen ⚡ Pulse Creator",
            bio = "Competitive Apex Predator & Cyberpunk speedrunner. Building custom mech keyboards and streaming everyday on PlayPulse! GG everyone.",
            pronouns = "he/they",
            gamingStatus = "Streaming: Apex Legends Season 24 Ranked",
            bannerGradientIndex = 0,
            avatarEmoji = "👾",
            avatarBorderColor = 0xFF00F0FF,
            level = 58,
            xp = 9420,
            nextLevelXp = 10000,
            reputationScore = 99,
            rankTitle = "Neon Master (Top 0.5%)",
            followersCount = 284500,
            followingCount = 312,
            totalLikesCount = "4.2M",
            coinsBalance = 4650,
            badges = listOf(
                UserBadge("b1", "Neon Pioneer", "⚡", "Member since Genesis Beta", 0xFF00F0FF),
                UserBadge("b2", "Esports MVP", "🏆", "Official Tournament Winner", 0xFFFFD700),
                UserBadge("b3", "Top 1% Creator", "🔥", "Over 1M monthly watch minutes", 0xFFFF007F),
                UserBadge("b4", "Voice Host", "🎙️", "Hosted 50+ Community Voice Lounges", 0xFF00FFA3),
                UserBadge("b5", "Mod Council", "🛡️", "Community Guardian & Moderator", 0xFF8A2BE2)
            ),
            socialLinks = listOf(
                SocialLink("Twitch", "twitch.tv/neonphantom", "https://twitch.tv", "🟣"),
                SocialLink("YouTube", "youtube.com/@NeonPhantom", "https://youtube.com", "🔴"),
                SocialLink("Discord", "discord.gg/neonpulse", "https://discord.gg", "👾"),
                SocialLink("X / Twitter", "@neonphantom_gg", "https://x.com", "✖️"),
                SocialLink("Steam", "steamcommunity.com/id/neonphantom", "https://steam.com", "🎮"),
                SocialLink("TikTok", "@neonphantom_clips", "https://tiktok.com", "🎵")
            ),
            hardwareSpecs = listOf(
                HardwareSpec("GPU", "NVIDIA GeForce RTX 4090 24GB", "🖥️"),
                HardwareSpec("CPU", "AMD Ryzen 9 7950X3D 16-Core", "⚡"),
                HardwareSpec("RAM", "64GB DDR5 6000MHz RGB", "💾"),
                HardwareSpec("Monitor", "34\" QD-OLED 240Hz Curved", "📺"),
                HardwareSpec("Mic & Audio", "Shure SM7B + GoXLR Mixer", "🎙️"),
                HardwareSpec("Custom Rig", "Cyber Neon Liquid Loop Build", "🧊")
            ),
            activeWidgets = listOf(
                ProfileWidget(ProfileWidgetType.PINNED_HIGHLIGHTS, "Pinned Video Highlights", isEnabled = true, order = 0),
                ProfileWidget(ProfileWidgetType.HARDWARE_SPECS, "Battlestation & Gear Specs", isEnabled = true, order = 1),
                ProfileWidget(ProfileWidgetType.BADGES_SHOWCASE, "Trophy & Creator Badges", isEnabled = true, order = 2),
                ProfileWidget(ProfileWidgetType.COMMUNITY_HUBS, "Active Guilds & Hubs", isEnabled = true, order = 3),
                ProfileWidget(ProfileWidgetType.CREATOR_STATS, "Live Creator Performance", isEnabled = true, order = 4)
            ),
            pinnedVideoIds = listOf("video_yt_1", "video_short_1")
        )
    )
    val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()

    // YouTube-style longform discovery feed videos
    private val _longVideos = MutableStateFlow(
        listOf(
            VideoItem(
                id = "video_yt_1",
                title = "The Ultimate Cyberpunk 2077 Ultra Modding Guide (2026 Graphics Overhaul)",
                description = "Over 120 photorealistic mods installed and tested on RTX 4090. Complete load order, ray tracing shader tweaks, and gameplay balancing for the next-gen Night City experience.",
                creatorId = "user_me",
                creatorName = "NeonPhantom",
                creatorHandle = "@neon_phantom",
                creatorAvatar = "👾",
                creatorBadge = "Verified Pro",
                type = VideoType.STANDARD_LONG,
                gameCategory = "Cyberpunk 2077",
                tags = listOf("Modding", "Graphics", "RTX", "Walkthrough"),
                viewsText = "842K views",
                likesCount = 48200,
                commentsCount = 1420,
                sharesCount = 6800,
                giftsCount = 312,
                uploadTimeAgo = "1 day ago",
                durationText = "24:18",
                gradientColors = listOf(0xFF0F2027, 0xFF203A43, 0xFF2C5364)
            ),
            VideoItem(
                id = "video_yt_2",
                title = "Apex Legends World Championship Finals: Last Circle 1v3 Clutch (Crazy Comms!)",
                description = "Listen to our raw squad voice comms as we maneuvered through 5 teams in the final thermal station ring. Strategy breakdown and weapon loadout details included.",
                creatorId = "creator_val",
                creatorName = "ValkyrieQueen",
                creatorHandle = "@valk_queen",
                creatorAvatar = "👑",
                creatorBadge = "Esports Champion",
                type = VideoType.STANDARD_LONG,
                gameCategory = "Apex Legends",
                tags = listOf("Esports", "Clutch", "Highlights", "Comms"),
                viewsText = "1.2M views",
                likesCount = 92400,
                commentsCount = 3100,
                sharesCount = 18400,
                giftsCount = 890,
                uploadTimeAgo = "3 days ago",
                durationText = "16:45",
                gradientColors = listOf(0xFF2C3E50, 0xFF4CA1AF, 0xFF141E30)
            ),
            VideoItem(
                id = "video_yt_3",
                title = "Elden Ring: Shadow of the Erdtree RL1 No-Hit All Bosses Marathon",
                description = "Rune Level 1 naked club run through all expansion remembrance bosses. Full boss timing breakdown, frame-perfect dodge guides, and talisman optimization.",
                creatorId = "creator_souls",
                creatorName = "AshenKnight",
                creatorHandle = "@ashen_souls",
                creatorAvatar = "⚔️",
                creatorBadge = "Speedrun Master",
                type = VideoType.STANDARD_LONG,
                gameCategory = "Elden Ring",
                tags = listOf("Soulsborne", "NoHit", "Speedrun", "Guide"),
                viewsText = "650K views",
                likesCount = 54100,
                commentsCount = 890,
                sharesCount = 4200,
                giftsCount = 430,
                uploadTimeAgo = "5 days ago",
                durationText = "48:12",
                gradientColors = listOf(0xFF1F1C2C, 0xFF928DAB, 0xFF0D0F18)
            ),
            VideoItem(
                id = "video_yt_4",
                title = "Building a Sub-Zero Liquid Nitrogen Cooled Gaming Rig in 24 Hours",
                description = "We pushed the latest silicon past 6.8GHz overclocking record. Smoke, frostbite alarms, and extreme benchmark scores.",
                creatorId = "creator_tech",
                creatorName = "SiliconForge",
                creatorHandle = "@silicon_forge",
                creatorAvatar = "⚡",
                creatorBadge = "Hardware Lab",
                type = VideoType.STANDARD_LONG,
                gameCategory = "Tech & Gear",
                tags = listOf("Hardware", "Overclock", "PCBuild", "Benchmarks"),
                viewsText = "390K views",
                likesCount = 31200,
                commentsCount = 650,
                sharesCount = 2900,
                giftsCount = 220,
                uploadTimeAgo = "1 week ago",
                durationText = "18:04",
                gradientColors = listOf(0xFF000428, 0xFF004E92, 0xFF0A1128)
            )
        )
    )
    val longVideos: StateFlow<List<VideoItem>> = _longVideos.asStateFlow()

    // TikTok-style Shorts ("Pulse Clips")
    private val _shortVideos = MutableStateFlow(
        listOf(
            VideoItem(
                id = "video_short_1",
                title = "Fastest Valorant Ace in Radiant rank with Sheriff ONLY 🎯🔥",
                description = "They tried rushing A site together and didn't expect the crosshair placement! #Valorant #Sheriff #Ace #PlayPulse",
                creatorId = "creator_val",
                creatorName = "ValkyrieQueen",
                creatorHandle = "@valk_queen",
                creatorAvatar = "👑",
                creatorBadge = "Radiant Pro",
                type = VideoType.SHORT_PULSE,
                gameCategory = "Valorant",
                tags = listOf("Valorant", "Ace", "Headshots", "Clips"),
                viewsText = "3.8M",
                likesCount = 412000,
                commentsCount = 14200,
                sharesCount = 58000,
                giftsCount = 1240,
                uploadTimeAgo = "3 hours ago",
                durationText = "0:34",
                soundTrackTitle = "Cyber Hyperpop - Bass Boosted Drop",
                gradientColors = listOf(0xFF0A0E27, 0xFF3F2B96, 0xFFE8115B)
            ),
            VideoItem(
                id = "video_short_2",
                title = "Accidentally discovered insane secret room in GTA 6 leak map! 🤯",
                description = "Look at this Easter Egg behind the neon diner in Vice Port! Tag your squad before this gets patched!",
                creatorId = "creator_gta",
                creatorName = "ViceRider",
                creatorHandle = "@vice_rider",
                creatorAvatar = "🏎️",
                creatorBadge = "Lore Hunter",
                type = VideoType.SHORT_PULSE,
                gameCategory = "GTA 6",
                tags = listOf("GTA6", "EasterEgg", "ViceCity", "GamerSecret"),
                viewsText = "5.1M",
                likesCount = 680000,
                commentsCount = 28100,
                sharesCount = 92000,
                giftsCount = 2100,
                uploadTimeAgo = "5 hours ago",
                durationText = "0:45",
                soundTrackTitle = "Synthwave Sunset - Night Drive Theme",
                gradientColors = listOf(0xFF1E0538, 0xFF65005A, 0xFFFF007F)
            ),
            VideoItem(
                id = "video_short_3",
                title = "When the boss has 1HP and your keyboard runs out of battery 💀",
                description = "I have never felt so much pain in my gaming life. Press F in comments to pay respects...",
                creatorId = "user_me",
                creatorName = "NeonPhantom",
                creatorHandle = "@neon_phantom",
                creatorAvatar = "👾",
                creatorBadge = "Verified Pro",
                type = VideoType.SHORT_PULSE,
                gameCategory = "Elden Ring",
                tags = listOf("GamingMemes", "Fail", "EldenRing", "Pain"),
                viewsText = "2.4M",
                likesCount = 345000,
                commentsCount = 9400,
                sharesCount = 41000,
                giftsCount = 980,
                uploadTimeAgo = "8 hours ago",
                durationText = "0:28",
                soundTrackTitle = "Emotional Piano & Record Scratch",
                gradientColors = listOf(0xFF161925, 0xFF23395B, 0xFF406E8E)
            ),
            VideoItem(
                id = "video_short_4",
                title = "Satisfying custom mechanical keyboard sound test (Hand lubed Holy Pandas) ⌨️✨",
                description = "Double gasket mounted brass plate typing ASMR. Headphones recommended for pure bliss.",
                creatorId = "creator_tech",
                creatorName = "KeebKing",
                creatorHandle = "@keeb_king",
                creatorAvatar = "⌨️",
                creatorBadge = "Maker",
                type = VideoType.SHORT_PULSE,
                gameCategory = "Tech & Gear",
                tags = listOf("KeyboardASMR", "Thock", "MechKeeb", "Tech"),
                viewsText = "1.8M",
                likesCount = 289000,
                commentsCount = 4200,
                sharesCount = 19000,
                giftsCount = 650,
                uploadTimeAgo = "12 hours ago",
                durationText = "0:52",
                soundTrackTitle = "Pure Keeb Thock Soundscape (Direct Input)",
                gradientColors = listOf(0xFF0F2027, 0xFF2C5364, 0xFF203A43)
            )
        )
    )
    val shortVideos: StateFlow<List<VideoItem>> = _shortVideos.asStateFlow()

    // Virtual Gifts for TikTok Live & Shorts
    val virtualGifts = listOf(
        VirtualGift("g1", "Neon Spark", "⚡", 10, "sparkle", 0xFF00F0FF),
        VirtualGift("g2", "GG Trophy", "🏆", 50, "trophy_burst", 0xFFFFD700),
        VirtualGift("g3", "Cyber Heart", "💖", 100, "heart_pulse", 0xFFFF007F),
        VirtualGift("g4", "Plasma Katana", "🗡️", 250, "sword_slash", 0xFF00FFA3),
        VirtualGift("g5", "Neon Dragon", "🐉", 1000, "dragon_flight", 0xFFFF5E00),
        VirtualGift("g6", "Pulse Champion Crown", "👑", 2500, "crown_fireworks", 0xFFFFD700)
    )

    // Discord-style Community Hubs
    private val _communityHubs = MutableStateFlow(
        listOf(
            CommunityHub(
                id = "hub_apex",
                name = "Apex Legends Pro Syndicate",
                tag = "#APEX-SYNDICATE",
                iconEmoji = "🎯",
                bannerGradient = listOf(0xFF8A2387, 0xFFE94057, 0xFFF27121),
                description = "The premier hub for scrims, squad recruitment, patch meta analysis, and live tournament watch parties.",
                memberCount = 84200,
                onlineCount = 14320,
                isJoined = true,
                verifiedPillar = true,
                categories = listOf(
                    HubCategory(
                        name = "📢 ANNOUNCEMENTS & RULES",
                        channels = listOf(
                            HubChannel("ch_announcements", "welcome-and-rules", ChannelType.ANNOUNCEMENT, "Official community guidelines & server rules"),
                            HubChannel("ch_patch_notes", "patch-meta-notes", ChannelType.TEXT, "Breakdown of weapon buffs and legend tuning")
                        )
                    ),
                    HubCategory(
                        name = "💬 TEXT CHANNELS",
                        channels = listOf(
                            HubChannel("ch_general", "general-lounge", ChannelType.TEXT, "Main hangout for Apex fans and chill banter", 3),
                            HubChannel("ch_clips", "clips-and-highlights", ChannelType.CLIPS_ONLY, "Post your best 1v3 clutches and movement clips"),
                            HubChannel("ch_lfg", "lfg-ranked-predator", ChannelType.TEXT, "Find competitive teammates with mic")
                        )
                    ),
                    HubCategory(
                        name = "🔊 VOICE CHANNELS (LIVE COMMS)",
                        channels = listOf(
                            HubChannel(
                                "ch_voice_squad1",
                                "🔊 Squad Comms #1 (Predator)",
                                ChannelType.VOICE,
                                "Low latency ranked voice room",
                                activeVoiceUsers = listOf(
                                    VoiceParticipant("vp1", "GhostRider", "👻", isSpeaking = true, roleName = "IGL"),
                                    VoiceParticipant("vp2", "ValkQueen", "👑", isSpeaking = false, roleName = "Fragger"),
                                    VoiceParticipant("vp3", "ApexSniper", "🎯", isSpeaking = true, roleName = "Anchor")
                                )
                            ),
                            HubChannel(
                                "ch_voice_chill",
                                "🔊 Chill Lounge & Music",
                                ChannelType.VOICE,
                                "Open mic hangout while gaming",
                                activeVoiceUsers = listOf(
                                    VoiceParticipant("vp4", "CyberSamurai", "🗡️", isSpeaking = false, roleName = "Member"),
                                    VoiceParticipant("vp5", "PixelPixie", "🧚", isSpeaking = false, roleName = "VIP")
                                )
                            )
                        )
                    )
                ),
                rules = listOf(
                    "Be respectful to all squadmates. Zero tolerance for toxicity or hate speech.",
                    "Keep comms clean during ranked tournaments and matches.",
                    "No self-promo without Creator Level 10+ badge.",
                    "Report griefing directly to Community Moderators with clip proof."
                )
            ),
            CommunityHub(
                id = "hub_cyberpunk",
                name = "Night City Modding & Lore",
                tag = "#NIGHT-CITY",
                iconEmoji = "🌆",
                bannerGradient = listOf(0xFF0F2027, 0xFF203A43, 0xFF2C5364),
                description = "Mod authors, cyberware theorycrafters, and virtual photographers pushing REDengine to the absolute limit.",
                memberCount = 52300,
                onlineCount = 8900,
                isJoined = true,
                categories = listOf(
                    HubCategory(
                        name = "💬 TEXT CHANNELS",
                        channels = listOf(
                            HubChannel("ch_cp_gen", "afterlife-bar", ChannelType.TEXT, "Chill discussions over synthetic chrome"),
                            HubChannel("ch_cp_mods", "mod-releases-and-help", ChannelType.TEXT, "Installation guides, textures, script mods")
                        )
                    ),
                    HubCategory(
                        name = "🔊 VOICE LOUNGE",
                        channels = listOf(
                            HubChannel(
                                "ch_cp_voice",
                                "🔊 Netrunner Pods",
                                ChannelType.VOICE,
                                "Virtual reality and modding voice chat",
                                activeVoiceUsers = listOf(
                                    VoiceParticipant("vp6", "Silverhand_Ghost", "🎸", isSpeaking = true, roleName = "Rockerboy")
                                )
                            )
                        )
                    )
                ),
                rules = listOf(
                    "Always credit original mod authors.",
                    "Use spoiler tags for phantom liberty story reveals."
                )
            ),
            CommunityHub(
                id = "hub_speedruns",
                name = "World Record Speedrunners",
                tag = "#SPEEDRUN-GUILD",
                iconEmoji = "⏱️",
                bannerGradient = listOf(0xFF1D2B64, 0xFFF8CDDA),
                description = "Frame-perfect tricks, route discovery, glitch bounties, and verified leaderboard runs.",
                memberCount = 38900,
                onlineCount = 6120,
                isJoined = false,
                categories = listOf(
                    HubCategory(
                        name = "💬 DISCUSSION",
                        channels = listOf(
                            HubChannel("ch_sr_routes", "route-engineering", ChannelType.TEXT, "TAS analysis and skip setups")
                        )
                    )
                ),
                rules = listOf("Video proof mandatory for all claimed splits.")
            )
        )
    )
    val communityHubs: StateFlow<List<CommunityHub>> = _communityHubs.asStateFlow()

    // Active Selected Hub & Channel for Discord-like view
    val selectedHubId = MutableStateFlow("hub_apex")
    val selectedChannelId = MutableStateFlow("ch_general")

    // Active Voice Room State
    data class ActiveVoiceRoomState(
        val isConnected: Boolean = false,
        val channelName: String = "",
        val hubName: String = "",
        val isMuted: Boolean = false,
        val isDeafened: Boolean = false,
        val isSpeaking: Boolean = false,
        val participants: List<VoiceParticipant> = emptyList()
    )
    private val _voiceRoomState = MutableStateFlow(ActiveVoiceRoomState())
    val voiceRoomState: StateFlow<ActiveVoiceRoomState> = _voiceRoomState.asStateFlow()

    // Comments per video ID
    private val _commentsMap = MutableStateFlow<Map<String, List<Comment>>>(
        mapOf(
            "video_yt_1" to listOf(
                Comment("c1", "video_yt_1", "CyberSamurai", "@cyber_samurai", "🗡️", "This visual preset made my game look straight out of 2030! The lighting bounce on wet pavement is insane.", likesCount = 284, timeAgo = "4h ago", authorBadge = "Mod Guru"),
                Comment("c2", "video_yt_1", "NeonPhantom", "@neon_phantom", "👾", "Thanks brother! Dropping the preset download link in our Community Hub now.", stickerEmoji = "🔥", likesCount = 142, isLiked = true, timeAgo = "3h ago", authorBadge = "Creator"),
                Comment("c3", "video_yt_1", "TechNoob99", "@technoob", "🎮", "Will this run smoothly on a laptop 4070?", stickerEmoji = "🤔", likesCount = 19, timeAgo = "1h ago")
            ),
            "video_short_1" to listOf(
                Comment("cs1", "video_short_1", "ProSlayer", "@pro_slayer", "⚡", "That flick on the third guy was ILLEGAL! Cleanest Sheriff clip this year.", stickerEmoji = "🎯", likesCount = 892, timeAgo = "1h ago", authorBadge = "Verified"),
                Comment("cs2", "video_short_1", "ValkyrieQueen", "@valk_queen", "👑", "Pre-aiming that angle every single round pays off! GG!", stickerEmoji = "🏆", likesCount = 540, timeAgo = "45m ago", authorBadge = "Creator"),
                Comment("cs3", "video_short_1", "EchoGamer", "@echo_gg", "🎧", "What sensitivity and DPI are you playing on?", likesCount = 84, timeAgo = "20m ago")
            )
        )
    )
    val commentsMap: StateFlow<Map<String, List<Comment>>> = _commentsMap.asStateFlow()

    // Live Stream state (TikTok Live style)
    private val _activeLiveStream = MutableStateFlow(
        LiveStream(
            id = "live_1",
            streamerName = "ValkyrieQueen",
            streamerHandle = "@valk_queen",
            streamerAvatar = "👑",
            title = "🔴 GRAND FINALS BO7: ROAD TO RADIANT TOP 10! | Drops ON & Real-time Squad Comms",
            gameCategory = "Valorant Champions",
            viewersCount = 38450,
            totalGiftsSent = 9420,
            streamTags = listOf("Ranked", "Radiant", "DropsEnabled", "NoSleepStream"),
            isLive = true,
            recentGifts = listOf(
                GiftEvent("CyberWolf", VirtualGift("g5", "Neon Dragon", "🐉", 1000, "dragon_flight", 0xFFFF5E00), comboCount = 3),
                GiftEvent("PixelPixie", VirtualGift("g2", "GG Trophy", "🏆", 50, "trophy_burst", 0xFFFFD700), comboCount = 12)
            ),
            chatMessages = listOf(
                LiveChatMessage("lm1", "ApexGamer", null, "THE CROSSHAIR PLACEMENT IS GODLIKE"),
                LiveChatMessage("lm2", "KaelenFan", "VIP", "LET'S GOOOOO VALK! 👑", isHighlighted = true),
                LiveChatMessage("lm3", "ShadowRunner", null, "Sent 5x GG Trophy! Push the site!"),
                LiveChatMessage("lm4", "NeonDragon", "Top Tipper", "Big clutch incoming!! Sent Neon Dragon", isHighlighted = true, tipAmountCoins = 1000),
                LiveChatMessage("lm5", "Spectator99", null, "Rank 8 in the world right now!!")
            )
        )
    )
    val activeLiveStream: StateFlow<LiveStream> = _activeLiveStream.asStateFlow()

    // Host Go Live Simulator state
    data class HostStreamState(
        val isStreaming: Boolean = false,
        val title: String = "Neon Night Arena - Custom Scrims & Giveaways!",
        val gameCategory: String = "Apex Legends",
        val viewerCount: Int = 1240,
        val isMuted: Boolean = false,
        val isCameraFront: Boolean = true,
        val giftsReceived: Int = 380,
        val streamDurationSeconds: Int = 145
    )
    private val _hostStreamState = MutableStateFlow(HostStreamState())
    val hostStreamState: StateFlow<HostStreamState> = _hostStreamState.asStateFlow()

    // End-to-End Encrypted Direct Messages
    private val _dmConversations = MutableStateFlow(
        listOf(
            DmConversation(
                id = "dm_valk",
                peerUser = UserProfile(
                    id = "user_valk",
                    username = "ValkyrieQueen",
                    handle = "@valk_queen",
                    displayName = "Valkyrie Queen 👑",
                    bio = "Esports Champion. Valorant Radiant & Apex Predator.",
                    avatarEmoji = "👑",
                    avatarBorderColor = 0xFFFF007F,
                    level = 72,
                    reputationScore = 100,
                    rankTitle = "Esports Grandmaster",
                    followersCount = 520000,
                    followingCount = 180,
                    totalLikesCount = "9.1M"
                ),
                lastMessage = "Hey Kaelen! Ready for our 2v2 tournament scrimmage tonight at 8 PM?",
                timestamp = "Just now",
                unreadCount = 1,
                isOnline = true,
                messages = listOf(
                    ChatMessage("m1", "user_valk", "ValkyrieQueen", "Hey Kaelen! Did you see the tournament bracket?", null, "18:24", isMe = false),
                    ChatMessage("m2", "user_me", "NeonPhantom", "Yeah! We are seeded #2 in Group A. Looking really strong.", null, "18:25", isMe = true),
                    ChatMessage("m3", "user_valk", "ValkyrieQueen", "Awesome! Ready for our 2v2 tournament scrimmage tonight at 8 PM?", "🔥", "18:28", isMe = false)
                )
            ),
            DmConversation(
                id = "dm_ashen",
                peerUser = UserProfile(
                    id = "user_ashen",
                    username = "AshenKnight",
                    handle = "@ashen_souls",
                    displayName = "Ashen Knight ⚔️",
                    bio = "Soulsborne runner & frame analysis pioneer.",
                    avatarEmoji = "⚔️",
                    avatarBorderColor = 0xFFFFBE0B,
                    level = 64,
                    reputationScore = 98,
                    rankTitle = "Speedrun Legend",
                    followersCount = 190000,
                    followingCount = 95,
                    totalLikesCount = "3.2M"
                ),
                lastMessage = "Sent the hitbox data for the final boss frame skip.",
                timestamp = "2h ago",
                unreadCount = 0,
                isOnline = false,
                messages = listOf(
                    ChatMessage("m4", "user_ashen", "AshenKnight", "Sent the hitbox data for the final boss frame skip.", "⚡", "16:10", isMe = false)
                )
            )
        )
    )
    val dmConversations: StateFlow<List<DmConversation>> = _dmConversations.asStateFlow()

    // Video Calling state
    data class CallSession(
        val isActive: Boolean = false,
        val isVideo: Boolean = true,
        val peerName: String = "Valkyrie Queen",
        val peerAvatar: String = "👑",
        val callDurationSeconds: Int = 24,
        val isMuted: Boolean = false,
        val isCameraOff: Boolean = false,
        val isE2EVerified: Boolean = true,
        val networkQuality: String = "Ultra HD 60FPS (Neon P2P)"
    )
    private val _currentCall = MutableStateFlow(CallSession())
    val currentCall: StateFlow<CallSession> = _currentCall.asStateFlow()

    // TikTok Shop-style Gaming Store
    private val _shopItems = MutableStateFlow(
        listOf(
            ShopItem("sp1", "PlayPulse Neon Cyber Headset Pro", "GEAR", 4500, 79.99, "🎧", "Wireless 2.4GHz ultra-low latency with RGB haptic feedback and crystal Discord-certified mic.", 4.9f, 3420),
            ShopItem("sp2", "RGB Magnetic Levitation Controller Stand", "GEAR", 2200, 39.99, "🎮", "Futuristic cyberpunk charging dock with ambient audio visualizer glow.", 4.8f, 1850),
            ShopItem("sp3", "Holographic Neon Master Avatar Frame", "AVATAR_AURA", 800, 9.99, "✨", "Animated cyber aura that pulses around your profile picture everywhere on PlayPulse.", 5.0f, 6200),
            ShopItem("sp4", "Super Streamer Gift Pack (2,500 Coins)", "COINS", 2500, 19.99, "🪙", "Instantly load your wallet with 2,500 coins + bonus Golden GG Trophy perk!", 4.9f, 15400),
            ShopItem("sp5", "PlayPulse Esports Team Pro Jersey 2026", "GEAR", 3200, 49.99, "👕", "Breathable dry-fit tournament jersey customized with your gamer tag.", 4.9f, 920),
            ShopItem("sp6", "Cyber Katana Chat Reaction Sticker Pack", "STICKERS", 400, 4.99, "🗡️", "16 exclusive animated stickers for stream chat, comments and encrypted DMs.", 4.8f, 4100)
        )
    )
    val shopItems: StateFlow<List<ShopItem>> = _shopItems.asStateFlow()

    // Gamified Reputation Quests
    private val _reputationQuests = MutableStateFlow(
        listOf(
            ReputationQuest("q1", "Squad Scout", "Join an active Community Voice Lounge and talk for 2 mins", 150, 50, 1, 1, isCompleted = true, isClaimed = false, iconEmoji = "🎙️"),
            ReputationQuest("q2", "Clip Enthusiast", "Watch 5 Pulse Short videos in the discovery reel", 100, 30, 4, 5, isCompleted = false, isClaimed = false, iconEmoji = "📱"),
            ReputationQuest("q3", "Community Patron", "Send any virtual gift in a live stream or clip", 300, 100, 1, 1, isCompleted = true, isClaimed = true, iconEmoji = "💖"),
            ReputationQuest("q4", "Clutch Creator", "Upload a gaming highlight clip to your profile", 500, 200, 0, 1, isCompleted = false, isClaimed = false, iconEmoji = "🚀")
        )
    )
    val reputationQuests: StateFlow<List<ReputationQuest>> = _reputationQuests.asStateFlow()

    // Creator Analytics
    private val _creatorAnalytics = MutableStateFlow(CreatorAnalytics())
    val creatorAnalytics: StateFlow<CreatorAnalytics> = _creatorAnalytics.asStateFlow()

    // Stickers library
    val availableStickers = listOf(
        Sticker("st1", "GG Fire", "🔥", "hype"),
        Sticker("st2", "Cyber Dragon", "🐉", "epic"),
        Sticker("st3", "Clutch God", "👑", "pro"),
        Sticker("st4", "Toxic Skull", "💀", "meme"),
        Sticker("st5", "Neon Heart", "💖", "love"),
        Sticker("st6", "Speed Run", "⚡", "hype"),
        Sticker("st7", "Target Locked", "🎯", "pro"),
        Sticker("st8", "Mind Blown", "🤯", "reaction"),
        Sticker("st9", "Salt Shaker", "🧂", "meme"),
        Sticker("st10", "Popcorn", "🍿", "chill")
    )

    // Global Countries for Login/Sign Up
    val supportedCountries = listOf(
        CountryInfo("United States", "US", "+1", "🇺🇸"),
        CountryInfo("Japan", "JP", "+81", "🇯🇵"),
        CountryInfo("United Kingdom", "GB", "+44", "🇬🇧"),
        CountryInfo("Germany", "DE", "+49", "🇩🇪"),
        CountryInfo("South Korea", "KR", "+82", "🇰🇷"),
        CountryInfo("France", "FR", "+33", "🇫🇷"),
        CountryInfo("Brazil", "BR", "+55", "🇧🇷"),
        CountryInfo("Canada", "CA", "+1", "🇨🇦"),
        CountryInfo("India", "IN", "+91", "🇮🇳"),
        CountryInfo("Australia", "AU", "+61", "🇦🇺"),
        CountryInfo("Spain", "ES", "+34", "🇪🇸"),
        CountryInfo("Mexico", "MX", "+52", "🇲🇽"),
        CountryInfo("Italy", "IT", "+39", "🇮🇹"),
        CountryInfo("Netherlands", "NL", "+31", "🇳🇱"),
        CountryInfo("Singapore", "SG", "+65", "🇸🇬"),
        CountryInfo("Sweden", "SE", "+46", "🇸🇪"),
        CountryInfo("Saudi Arabia", "SA", "+966", "🇸🇦"),
        CountryInfo("United Arab Emirates", "AE", "+971", "🇦🇪"),
        CountryInfo("Indonesia", "ID", "+62", "🇮🇩"),
        CountryInfo("Philippines", "PH", "+63", "🇵🇭")
    )

    // Current App Language
    private val _currentLanguage = MutableStateFlow("English")
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    // Interactive Functions
    fun setLanguage(lang: String) {
        _currentLanguage.value = lang
    }

    fun toggleLikeVideo(videoId: String, isShort: Boolean) {
        if (isShort) {
            _shortVideos.update { list ->
                list.map {
                    if (it.id == videoId) {
                        val newLiked = !it.isLiked
                        it.copy(
                            isLiked = newLiked,
                            likesCount = if (newLiked) it.likesCount + 1 else it.likesCount - 1
                        )
                    } else it
                }
            }
        } else {
            _longVideos.update { list ->
                list.map {
                    if (it.id == videoId) {
                        val newLiked = !it.isLiked
                        it.copy(
                            isLiked = newLiked,
                            likesCount = if (newLiked) it.likesCount + 1 else it.likesCount - 1
                        )
                    } else it
                }
            }
        }
    }

    fun toggleFollowCreator(creatorId: String) {
        _shortVideos.update { list ->
            list.map { if (it.creatorId == creatorId) it.copy(isFollowed = !it.isFollowed) else it }
        }
        _longVideos.update { list ->
            list.map { if (it.creatorId == creatorId) it.copy(isFollowed = !it.isFollowed) else it }
        }
    }

    fun addComment(videoId: String, text: String, stickerEmoji: String? = null) {
        val user = _currentUser.value
        val newComment = Comment(
            id = "c_${System.currentTimeMillis()}",
            videoId = videoId,
            authorName = user.displayName,
            authorHandle = user.handle,
            authorAvatar = user.avatarEmoji,
            text = text,
            stickerEmoji = stickerEmoji,
            likesCount = 0,
            isLiked = false,
            timeAgo = "Just now",
            authorBadge = "Member"
        )
        _commentsMap.update { map ->
            val existing = map[videoId] ?: emptyList()
            map + (videoId to (listOf(newComment) + existing))
        }
    }

    fun sendLiveGift(gift: VirtualGift) {
        val currentCoins = _currentUser.value.coinsBalance
        if (currentCoins >= gift.coinCost) {
            _currentUser.update { it.copy(coinsBalance = it.coinsBalance - gift.coinCost) }
            val newEvent = GiftEvent(senderName = _currentUser.value.displayName, gift = gift, comboCount = (1..5).random())
            _activeLiveStream.update { stream ->
                stream.copy(
                    totalGiftsSent = stream.totalGiftsSent + 1,
                    recentGifts = listOf(newEvent) + stream.recentGifts.take(4),
                    chatMessages = stream.chatMessages + LiveChatMessage(
                        id = "gift_${System.currentTimeMillis()}",
                        senderName = _currentUser.value.displayName,
                        senderBadge = "VIP Patron",
                        message = "Sent ${gift.emoji} ${gift.name}!",
                        isHighlighted = true,
                        tipAmountCoins = gift.coinCost
                    )
                )
            }
        }
    }

    fun sendLiveChatMessage(msg: String) {
        _activeLiveStream.update { stream ->
            stream.copy(
                chatMessages = stream.chatMessages + LiveChatMessage(
                    id = "msg_${System.currentTimeMillis()}",
                    senderName = _currentUser.value.displayName,
                    senderBadge = "VIP",
                    message = msg
                )
            )
        }
    }

    fun joinVoiceChannel(hub: CommunityHub, channel: HubChannel) {
        val myParticipant = VoiceParticipant(
            id = "my_voice",
            name = _currentUser.value.displayName,
            avatarEmoji = _currentUser.value.avatarEmoji,
            isSpeaking = false,
            isMuted = false,
            isDeafened = false,
            roleName = "Neon Master"
        )
        _voiceRoomState.value = ActiveVoiceRoomState(
            isConnected = true,
            channelName = channel.name,
            hubName = hub.name,
            isMuted = false,
            isDeafened = false,
            isSpeaking = false,
            participants = channel.activeVoiceUsers + myParticipant
        )
    }

    fun toggleVoiceMute() {
        _voiceRoomState.update { it.copy(isMuted = !it.isMuted) }
    }

    fun toggleVoiceDeafen() {
        _voiceRoomState.update { it.copy(isDeafened = !it.isDeafened) }
    }

    fun leaveVoiceChannel() {
        _voiceRoomState.value = ActiveVoiceRoomState(isConnected = false)
    }

    fun toggleJoinHub(hubId: String) {
        _communityHubs.update { list ->
            list.map {
                if (it.id == hubId) it.copy(isJoined = !it.isJoined) else it
            }
        }
    }

    fun sendDmMessage(conversationId: String, text: String, stickerEmoji: String? = null) {
        val newMsg = ChatMessage(
            id = "dm_msg_${System.currentTimeMillis()}",
            senderId = _currentUser.value.id,
            senderName = _currentUser.value.displayName,
            text = text,
            stickerEmoji = stickerEmoji,
            timestamp = "Just now",
            isMe = true,
            isEncrypted = true
        )
        _dmConversations.update { list ->
            list.map { conv ->
                if (conv.id == conversationId) {
                    conv.copy(
                        lastMessage = if (text.isNotBlank()) text else "[Sticker]",
                        timestamp = "Just now",
                        messages = conv.messages + newMsg
                    )
                } else conv
            }
        }
    }

    fun startCall(peerUser: UserProfile, isVideo: Boolean) {
        _currentCall.value = CallSession(
            isActive = true,
            isVideo = isVideo,
            peerName = peerUser.displayName,
            peerAvatar = peerUser.avatarEmoji,
            callDurationSeconds = 1,
            isMuted = false,
            isCameraOff = false,
            isE2EVerified = true
        )
    }

    fun endCall() {
        _currentCall.value = CallSession(isActive = false)
    }

    fun toggleCallMute() {
        _currentCall.update { it.copy(isMuted = !it.isMuted) }
    }

    fun toggleCallCamera() {
        _currentCall.update { it.copy(isCameraOff = !it.isCameraOff) }
    }

    fun claimQuest(questId: String) {
        _reputationQuests.update { list ->
            list.map {
                if (it.id == questId && it.isCompleted && !it.isClaimed) {
                    // grant rewards
                    _currentUser.update { u ->
                        u.copy(
                            xp = u.xp + it.xpReward,
                            coinsBalance = u.coinsBalance + it.coinReward
                        )
                    }
                    it.copy(isClaimed = true)
                } else it
            }
        }
    }

    fun buyShopItem(item: ShopItem): Boolean {
        val user = _currentUser.value
        if (item.category == "COINS") {
            // Simulated in-app purchase of coins
            _currentUser.update { it.copy(coinsBalance = it.coinsBalance + item.priceCoins) }
            return true
        } else if (user.coinsBalance >= item.priceCoins) {
            _currentUser.update { it.copy(coinsBalance = it.coinsBalance - item.priceCoins) }
            return true
        }
        return false
    }

    fun updateProfileCustomization(
        displayName: String,
        bio: String,
        pronouns: String,
        gamingStatus: String,
        bannerIndex: Int,
        avatarEmoji: String,
        borderColor: Long,
        widgets: List<ProfileWidget>
    ) {
        _currentUser.update {
            it.copy(
                displayName = displayName,
                bio = bio,
                pronouns = pronouns,
                gamingStatus = gamingStatus,
                bannerGradientIndex = bannerIndex,
                avatarEmoji = avatarEmoji,
                avatarBorderColor = borderColor,
                activeWidgets = widgets
            )
        }
    }

    fun uploadVideo(
        title: String,
        description: String,
        category: String,
        tags: List<String>,
        isShort: Boolean
    ) {
        val user = _currentUser.value
        val newVideo = VideoItem(
            id = "vid_${System.currentTimeMillis()}",
            title = title,
            description = description,
            creatorId = user.id,
            creatorName = user.displayName,
            creatorHandle = user.handle,
            creatorAvatar = user.avatarEmoji,
            creatorBadge = "Creator",
            type = if (isShort) VideoType.SHORT_PULSE else VideoType.STANDARD_LONG,
            gameCategory = category,
            tags = tags,
            viewsText = "1 view",
            likesCount = 1,
            isLiked = true,
            commentsCount = 0,
            sharesCount = 0,
            giftsCount = 0,
            uploadTimeAgo = "Just now",
            durationText = if (isShort) "0:30" else "12:45",
            gradientColors = listOf(0xFF0F2027, 0xFF2C5364, 0xFF203A43)
        )
        if (isShort) {
            _shortVideos.update { listOf(newVideo) + it }
        } else {
            _longVideos.update { listOf(newVideo) + it }
        }
    }
}
