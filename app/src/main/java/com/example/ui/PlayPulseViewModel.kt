package com.example.ui

import androidx.lifecycle.ViewModel
import com.example.data.PlayPulseRepository
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class NavigationTab {
    WATCH,
    SHORTS,
    HUBS,
    LIVE,
    PROFILE
}

class PlayPulseViewModel(
    val repository: PlayPulseRepository = PlayPulseRepository()
) : ViewModel() {

    val currentUser = repository.currentUser
    val longVideos = repository.longVideos
    val shortVideos = repository.shortVideos
    val communityHubs = repository.communityHubs
    val voiceRoomState = repository.voiceRoomState
    val commentsMap = repository.commentsMap
    val activeLiveStream = repository.activeLiveStream
    val dmConversations = repository.dmConversations
    val currentCall = repository.currentCall
    val shopItems = repository.shopItems
    val reputationQuests = repository.reputationQuests
    val creatorAnalytics = repository.creatorAnalytics
    val currentLanguage = repository.currentLanguage
    val virtualGifts = repository.virtualGifts
    val availableStickers = repository.availableStickers
    val supportedCountries = repository.supportedCountries

    // Main Navigation state
    private val _currentTab = MutableStateFlow(NavigationTab.WATCH)
    val currentTab: StateFlow<NavigationTab> = _currentTab.asStateFlow()

    fun setTab(tab: NavigationTab) {
        _currentTab.value = tab
    }

    // Category filter for Watch feed
    private val _selectedCategory = MutableStateFlow("All Games")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    fun setSelectedCategory(cat: String) {
        _selectedCategory.value = cat
    }

    // Search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun setSearchQuery(q: String) {
        _searchQuery.value = q
    }

    // Active long video watch player
    private val _activeWatchVideo = MutableStateFlow<VideoItem?>(null)
    val activeWatchVideo: StateFlow<VideoItem?> = _activeWatchVideo.asStateFlow()

    fun selectWatchVideo(video: VideoItem?) {
        _activeWatchVideo.value = video
    }

    // Selected hub & channel in Community Hubs
    val selectedHubId = repository.selectedHubId
    val selectedChannelId = repository.selectedChannelId

    fun selectHub(hubId: String) {
        selectedHubId.value = hubId
        val hub = communityHubs.value.find { it.id == hubId }
        val firstCh = hub?.categories?.firstOrNull()?.channels?.firstOrNull()?.id ?: "ch_general"
        selectedChannelId.value = firstCh
    }

    fun selectChannel(channelId: String) {
        selectedChannelId.value = channelId
    }

    // Sheets & Dialogs
    private val _activeCommentsVideoId = MutableStateFlow<String?>(null)
    val activeCommentsVideoId: StateFlow<String?> = _activeCommentsVideoId.asStateFlow()

    fun openComments(videoId: String) {
        _activeCommentsVideoId.value = videoId
    }

    fun closeComments() {
        _activeCommentsVideoId.value = null
    }

    private val _isGiftSheetOpen = MutableStateFlow(false)
    val isGiftSheetOpen: StateFlow<Boolean> = _isGiftSheetOpen.asStateFlow()

    fun openGiftSheet() {
        _isGiftSheetOpen.value = true
    }

    fun closeGiftSheet() {
        _isGiftSheetOpen.value = false
    }

    // Active DM chat
    private val _activeDmId = MutableStateFlow<String?>(null)
    val activeDmId: StateFlow<String?> = _activeDmId.asStateFlow()

    fun openDmChat(dmId: String) {
        _activeDmId.value = dmId
    }

    fun closeDmChat() {
        _activeDmId.value = null
    }

    // DMs list dialog
    private val _isDmsListOpen = MutableStateFlow(false)
    val isDmsListOpen: StateFlow<Boolean> = _isDmsListOpen.asStateFlow()

    fun openDmsList() {
        _isDmsListOpen.value = true
    }

    fun closeDmsList() {
        _isDmsListOpen.value = false
    }

    // Modals
    val isEditProfileOpen = MutableStateFlow(false)
    val isCreateUploadOpen = MutableStateFlow(false)
    val isHostGoLiveOpen = MutableStateFlow(false)
    val isShopSheetOpen = MutableStateFlow(false)
    val isAuthModalOpen = MutableStateFlow(false)
    val isLanguageModalOpen = MutableStateFlow(false)
    val isModerationSheetOpen = MutableStateFlow(false)
    val isDarkMode = MutableStateFlow(true)

    // User authentication status
    private val _isLoggedIn = MutableStateFlow(true)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    fun simulateOAuthLogin(provider: String) {
        _isLoggedIn.value = true
        isAuthModalOpen.value = false
    }

    fun toggleDarkMode() {
        isDarkMode.value = !isDarkMode.value
    }
}
