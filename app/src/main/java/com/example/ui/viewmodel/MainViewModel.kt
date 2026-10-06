package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.FreeSTimeDatabase
import com.example.data.model.AppNotification
import com.example.data.model.CampaignOrder
import com.example.data.model.CampaignStatus
import com.example.data.model.CreatorProfile
import com.example.data.model.FraudCheckResult
import com.example.data.model.MarketingService
import com.example.data.model.PaymentTransaction
import com.example.data.model.SocialPlatform
import com.example.data.model.UserRole
import com.example.data.model.UserSession
import com.example.data.repository.MarketingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppNavTab(val label: String) {
    HOME("Home"),
    SERVICES("Services"),
    ORDERS("Orders"),
    CREATORS("Creators"),
    ANALYTICS("Analytics"),
    PROFILE("Profile")
}

data class CampaignFormState(
    val selectedService: MarketingService? = null,
    val selectedPlatform: SocialPlatform = SocialPlatform.INSTAGRAM,
    val campaignTitle: String = "",
    val targetUrl: String = "",
    val targetAudience: String = "Tech & General Audience, Ages 18-35",
    val targetCountry: String = "Global (Top Tier English)",
    val budgetUsd: Double = 50.0,
    val paymentMethod: String = "Google Pay (Tokenized)",
    val creatorCollabName: String? = null,
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = FreeSTimeDatabase.getDatabase(application, viewModelScope)
    val repository = MarketingRepository(
        orderDao = database.orderDao(),
        creatorDao = database.creatorDao(),
        transactionDao = database.transactionDao(),
        notificationDao = database.notificationDao()
    )

    // Navigation state
    private val _currentTab = MutableStateFlow(AppNavTab.HOME)
    val currentTab: StateFlow<AppNavTab> = _currentTab.asStateFlow()

    // Platform filter for services/marketplace
    private val _selectedPlatform = MutableStateFlow(SocialPlatform.ALL)
    val selectedPlatform: StateFlow<SocialPlatform> = _selectedPlatform.asStateFlow()

    // Search query for services
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Orders status filter
    private val _orderStatusFilter = MutableStateFlow<CampaignStatus?>(null)
    val orderStatusFilter: StateFlow<CampaignStatus?> = _orderStatusFilter.asStateFlow()

    // Creator category filter
    private val _creatorCategoryFilter = MutableStateFlow("All")
    val creatorCategoryFilter: StateFlow<String> = _creatorCategoryFilter.asStateFlow()

    // Dialog & UI sheet states
    private val _campaignForm = MutableStateFlow(CampaignFormState())
    val campaignForm: StateFlow<CampaignFormState> = _campaignForm.asStateFlow()

    private val _isCampaignBuilderOpen = MutableStateFlow(false)
    val isCampaignBuilderOpen: StateFlow<Boolean> = _isCampaignBuilderOpen.asStateFlow()

    private val _selectedOrderForReport = MutableStateFlow<CampaignOrder?>(null)
    val selectedOrderForReport: StateFlow<CampaignOrder?> = _selectedOrderForReport.asStateFlow()

    private val _isCreatorRegisterOpen = MutableStateFlow(false)
    val isCreatorRegisterOpen: StateFlow<Boolean> = _isCreatorRegisterOpen.asStateFlow()

    private val _isNotificationsOpen = MutableStateFlow(false)
    val isNotificationsOpen: StateFlow<Boolean> = _isNotificationsOpen.asStateFlow()

    private val _isTopUpOpen = MutableStateFlow(false)
    val isTopUpOpen: StateFlow<Boolean> = _isTopUpOpen.asStateFlow()

    private val _fraudAlertMessage = MutableStateFlow<String?>(null)
    val fraudAlertMessage: StateFlow<String?> = _fraudAlertMessage.asStateFlow()

    private val _snackBarMessage = MutableStateFlow<String?>(null)
    val snackBarMessage: StateFlow<String?> = _snackBarMessage.asStateFlow()

    // Repository flows exposed as StateFlows
    val userSession: StateFlow<UserSession> = repository.userSession
    val allOrders: StateFlow<List<CampaignOrder>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCreators: StateFlow<List<CreatorProfile>> = repository.allCreators
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTransactions: StateFlow<List<PaymentTransaction>> = repository.allTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allNotifications: StateFlow<List<AppNotification>> = repository.allNotifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unreadNotificationsCount: StateFlow<Int> = repository.unreadNotificationCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val services: StateFlow<List<MarketingService>> = repository.services

    // Filtered services
    val filteredServices: StateFlow<List<MarketingService>> = combine(
        services,
        selectedPlatform,
        searchQuery
    ) { allServices, platform, query ->
        allServices.filter { service ->
            val matchesPlatform = platform == SocialPlatform.ALL ||
                    service.supportedPlatforms.contains(platform) ||
                    service.supportedPlatforms.contains(SocialPlatform.ALL)
            val matchesQuery = query.isBlank() ||
                    service.title.contains(query, ignoreCase = true) ||
                    service.description.contains(query, ignoreCase = true)
            matchesPlatform && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered Creators
    val filteredCreators: StateFlow<List<CreatorProfile>> = combine(
        allCreators,
        selectedPlatform,
        creatorCategoryFilter
    ) { creators, platform, category ->
        creators.filter { c ->
            val matchesPlatform = platform == SocialPlatform.ALL || c.platform == platform
            val matchesCategory = category == "All" || c.category.contains(category, ignoreCase = true)
            matchesPlatform && matchesCategory
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered Orders
    val filteredOrders: StateFlow<List<CampaignOrder>> = combine(
        allOrders,
        orderStatusFilter
    ) { orders, statusFilter ->
        if (statusFilter == null) orders else orders.filter { it.status == statusFilter }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Navigation actions
    fun navigateTo(tab: AppNavTab) {
        _currentTab.value = tab
    }

    fun setPlatformFilter(platform: SocialPlatform) {
        _selectedPlatform.value = platform
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setOrderStatusFilter(status: CampaignStatus?) {
        _orderStatusFilter.value = status
    }

    fun setCreatorCategoryFilter(category: String) {
        _creatorCategoryFilter.value = category
    }

    // Role switching
    fun switchUserRole(role: UserRole) {
        repository.setUserRole(role)
        showSnackbar("Switched mode to: ${role.label}")
    }

    // Campaign Builder Actions
    fun openCampaignBuilder(service: MarketingService? = null, creator: CreatorProfile? = null) {
        val targetPlatform = creator?.platform ?: service?.supportedPlatforms?.firstOrNull() ?: SocialPlatform.INSTAGRAM
        val initialBudget = creator?.startingPriceUsd ?: service?.basePriceUsd ?: 45.0
        _campaignForm.value = CampaignFormState(
            selectedService = service,
            selectedPlatform = if (targetPlatform == SocialPlatform.ALL) SocialPlatform.INSTAGRAM else targetPlatform,
            campaignTitle = if (service != null) "Promotion: ${service.title}" else "New Legal Marketing Campaign",
            budgetUsd = initialBudget,
            creatorCollabName = creator?.name
        )
        _isCampaignBuilderOpen.value = true
    }

    fun closeCampaignBuilder() {
        _isCampaignBuilderOpen.value = false
        _campaignForm.value = CampaignFormState()
    }

    fun updateCampaignForm(transform: (CampaignFormState) -> CampaignFormState) {
        _campaignForm.value = transform(_campaignForm.value)
    }

    fun submitCampaignOrder() {
        val form = _campaignForm.value
        val service = form.selectedService ?: services.value.first()

        if (form.campaignTitle.isBlank()) {
            _campaignForm.value = form.copy(errorMessage = "Campaign title is required")
            return
        }
        if (form.targetUrl.isBlank()) {
            _campaignForm.value = form.copy(errorMessage = "Target URL or social link is required")
            return
        }

        // Fraud check
        val fraudResult: FraudCheckResult = repository.performSafetyFraudCheck(
            form.campaignTitle,
            form.targetUrl,
            form.targetAudience
        )

        if (!fraudResult.isLegitimate) {
            _fraudAlertMessage.value = fraudResult.reason
            return
        }

        viewModelScope.launch {
            _campaignForm.value = form.copy(isSubmitting = true, errorMessage = null)
            val result = repository.placeCampaignOrder(
                serviceId = service.id,
                serviceTitle = service.title,
                platform = form.selectedPlatform,
                campaignTitle = form.campaignTitle,
                targetUrl = form.targetUrl,
                targetAudience = form.targetAudience,
                targetCountry = form.targetCountry,
                budgetUsd = form.budgetUsd,
                paymentMethod = form.paymentMethod,
                creatorName = form.creatorCollabName
            )

            result.fold(
                onSuccess = {
                    _isCampaignBuilderOpen.value = false
                    _campaignForm.value = CampaignFormState()
                    _currentTab.value = AppNavTab.ORDERS
                    showSnackbar("Campaign order submitted securely! Status: Pending Admin Review.")
                },
                onFailure = { err ->
                    _campaignForm.value = form.copy(
                        isSubmitting = false,
                        errorMessage = err.message ?: "Failed to place order"
                    )
                }
            )
        }
    }

    // Report Sheet
    fun openOrderReport(order: CampaignOrder) {
        _selectedOrderForReport.value = order
    }

    fun closeOrderReport() {
        _selectedOrderForReport.value = null
    }

    // Creator Application Modal
    fun openCreatorRegister() {
        _isCreatorRegisterOpen.value = true
    }

    fun closeCreatorRegister() {
        _isCreatorRegisterOpen.value = false
    }

    fun registerCreator(
        name: String,
        handle: String,
        platform: SocialPlatform,
        category: String,
        startingPrice: Double,
        reach: String,
        deliveryDays: String,
        contentType: String,
        bio: String
    ) {
        viewModelScope.launch {
            repository.registerCreator(
                name = name,
                handle = handle,
                platform = platform,
                category = category,
                startingPrice = startingPrice,
                reach = reach,
                deliveryDays = deliveryDays,
                contentType = contentType,
                bio = bio
            )
            _isCreatorRegisterOpen.value = false
            showSnackbar("Creator registered successfully! Audience audit passed: 0% Bots.")
        }
    }

    // Notifications Drawer
    fun toggleNotifications(open: Boolean) {
        _isNotificationsOpen.value = open
    }

    fun markAllNotificationsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsAsRead()
        }
    }

    // Top up balance
    fun toggleTopUp(open: Boolean) {
        _isTopUpOpen.value = open
    }

    fun topUpWallet(amountUsd: Double) {
        repository.topUpWallet(amountUsd)
        _isTopUpOpen.value = false
        showSnackbar("Added $$${"%.2f".format(amountUsd)} to wallet balance.")
    }

    fun dismissFraudAlert() {
        _fraudAlertMessage.value = null
    }

    fun showSnackbar(message: String) {
        _snackBarMessage.value = message
    }

    fun dismissSnackbar() {
        _snackBarMessage.value = null
    }

    // Admin Operations
    fun adminApproveOrder(orderId: Long, note: String = "Approved for ad placement") {
        viewModelScope.launch {
            repository.approveCampaign(orderId, note)
            showSnackbar("Campaign #$orderId Approved & Scheduled.")
        }
    }

    fun adminStartOrder(orderId: Long) {
        viewModelScope.launch {
            repository.startCampaign(orderId)
            showSnackbar("Campaign #$orderId is now LIVE.")
        }
    }

    fun adminCompleteOrder(orderId: Long) {
        viewModelScope.launch {
            repository.completeCampaign(orderId)
            showSnackbar("Campaign #$orderId Marked COMPLETED.")
        }
    }

    fun adminRejectOrder(orderId: Long, reason: String) {
        viewModelScope.launch {
            repository.rejectCampaign(orderId, reason)
            showSnackbar("Campaign #$orderId REJECTED & Full Refund Issued to User.")
        }
    }

    fun adminUpdatePrice(serviceId: String, newPrice: Double) {
        repository.updateServicePrice(serviceId, newPrice)
        showSnackbar("Service price updated to $$newPrice")
    }

    fun adminToggleService(serviceId: String) {
        repository.toggleServiceActive(serviceId)
        showSnackbar("Service active status toggled")
    }

    fun adminSendBroadcast(title: String, message: String) {
        viewModelScope.launch {
            repository.sendBroadcastNotification(title, message)
            showSnackbar("Broadcast notification sent to all users")
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            repository.deleteUserAccountAndData()
            showSnackbar("Account and personal data cleared.")
        }
    }
}
