package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.UserRole
import com.example.ui.components.CampaignBuilderSheet
import com.example.ui.components.CampaignReportSheet
import com.example.ui.components.CreatorRegisterSheet
import com.example.ui.components.FraudWarningDialog
import com.example.ui.components.FreeSTimeTopBar
import com.example.ui.components.NotificationsSheet
import com.example.ui.components.TopUpWalletDialog
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.CreatorsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OrdersScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ServicesScreen
import com.example.ui.theme.FreeSTimeTheme
import com.example.ui.viewmodel.AppNavTab
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FreeSTimeTheme {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppScreen(viewModel: MainViewModel) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val userSession by viewModel.userSession.collectAsStateWithLifecycle()
    val unreadNotifs by viewModel.unreadNotificationsCount.collectAsStateWithLifecycle()

    val filteredServices by viewModel.filteredServices.collectAsStateWithLifecycle()
    val filteredOrders by viewModel.filteredOrders.collectAsStateWithLifecycle()
    val allOrders by viewModel.allOrders.collectAsStateWithLifecycle()
    val filteredCreators by viewModel.filteredCreators.collectAsStateWithLifecycle()
    val allTransactions by viewModel.allTransactions.collectAsStateWithLifecycle()
    val allNotifications by viewModel.allNotifications.collectAsStateWithLifecycle()
    val servicesCatalog by viewModel.services.collectAsStateWithLifecycle()

    val selectedPlatform by viewModel.selectedPlatform.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val orderStatusFilter by viewModel.orderStatusFilter.collectAsStateWithLifecycle()
    val creatorCategoryFilter by viewModel.creatorCategoryFilter.collectAsStateWithLifecycle()

    val campaignForm by viewModel.campaignForm.collectAsStateWithLifecycle()
    val isCampaignBuilderOpen by viewModel.isCampaignBuilderOpen.collectAsStateWithLifecycle()
    val selectedOrderForReport by viewModel.selectedOrderForReport.collectAsStateWithLifecycle()
    val isCreatorRegisterOpen by viewModel.isCreatorRegisterOpen.collectAsStateWithLifecycle()
    val isNotificationsOpen by viewModel.isNotificationsOpen.collectAsStateWithLifecycle()
    val isTopUpOpen by viewModel.isTopUpOpen.collectAsStateWithLifecycle()
    val fraudAlertMessage by viewModel.fraudAlertMessage.collectAsStateWithLifecycle()
    val snackbarMsg by viewModel.snackBarMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMsg) {
        snackbarMsg?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.dismissSnackbar()
        }
    }

    // Android back navigation handler
    BackHandler(enabled = currentTab != AppNavTab.HOME) {
        viewModel.navigateTo(AppNavTab.HOME)
    }

    Scaffold(
        topBar = {
            FreeSTimeTopBar(
                userSession = userSession,
                unreadNotifications = unreadNotifs,
                onRoleChange = { viewModel.switchUserRole(it) },
                onOpenNotifications = { viewModel.toggleNotifications(true) },
                onOpenWalletTopUp = { viewModel.toggleTopUp(true) }
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("bottom_nav_bar")
            ) {
                NavigationBarItem(
                    selected = currentTab == AppNavTab.HOME && userSession.role != UserRole.ADMIN,
                    onClick = { viewModel.navigateTo(AppNavTab.HOME) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home", modifier = Modifier.size(20.dp)) },
                    label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                    modifier = Modifier.testTag("nav_tab_home")
                )
                NavigationBarItem(
                    selected = currentTab == AppNavTab.SERVICES,
                    onClick = { viewModel.navigateTo(AppNavTab.SERVICES) },
                    icon = { Icon(Icons.Default.Storefront, contentDescription = "Services", modifier = Modifier.size(20.dp)) },
                    label = { Text("Services", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                    modifier = Modifier.testTag("nav_tab_services")
                )
                NavigationBarItem(
                    selected = currentTab == AppNavTab.ORDERS,
                    onClick = { viewModel.navigateTo(AppNavTab.ORDERS) },
                    icon = { Icon(Icons.Default.Layers, contentDescription = "Orders", modifier = Modifier.size(20.dp)) },
                    label = { Text("Orders", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                    modifier = Modifier.testTag("nav_tab_orders")
                )
                NavigationBarItem(
                    selected = currentTab == AppNavTab.CREATORS,
                    onClick = { viewModel.navigateTo(AppNavTab.CREATORS) },
                    icon = { Icon(Icons.Default.People, contentDescription = "Creators", modifier = Modifier.size(20.dp)) },
                    label = { Text("Creators", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                    modifier = Modifier.testTag("nav_tab_creators")
                )
                NavigationBarItem(
                    selected = currentTab == AppNavTab.ANALYTICS,
                    onClick = { viewModel.navigateTo(AppNavTab.ANALYTICS) },
                    icon = { Icon(Icons.Default.Analytics, contentDescription = "Analytics", modifier = Modifier.size(20.dp)) },
                    label = { Text("Analytics", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                    modifier = Modifier.testTag("nav_tab_analytics")
                )
                NavigationBarItem(
                    selected = currentTab == AppNavTab.PROFILE,
                    onClick = { viewModel.navigateTo(AppNavTab.PROFILE) },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile", modifier = Modifier.size(20.dp)) },
                    label = { Text("Profile", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                    modifier = Modifier.testTag("nav_tab_profile")
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Role-based screen routing
            if (userSession.role == UserRole.ADMIN && currentTab == AppNavTab.HOME) {
                // In admin mode, the primary landing can be the comprehensive Admin Center
                AdminScreen(
                    orders = allOrders,
                    services = servicesCatalog,
                    onApproveOrder = { id, note -> viewModel.adminApproveOrder(id, note) },
                    onStartOrder = { id -> viewModel.adminStartOrder(id) },
                    onCompleteOrder = { id -> viewModel.adminCompleteOrder(id) },
                    onRejectOrder = { id, reason -> viewModel.adminRejectOrder(id, reason) },
                    onUpdatePrice = { id, price -> viewModel.adminUpdatePrice(id, price) },
                    onToggleService = { id -> viewModel.adminToggleService(id) },
                    onSendBroadcast = { title, msg -> viewModel.adminSendBroadcast(title, msg) }
                )
            } else {
                when (currentTab) {
                    AppNavTab.HOME -> {
                        HomeScreen(
                            featuredServices = servicesCatalog,
                            activeOrders = allOrders,
                            onSelectPlatform = { viewModel.setPlatformFilter(it) },
                            onStartCampaign = { viewModel.openCampaignBuilder(it) },
                            onViewOrderReport = { viewModel.openOrderReport(it) },
                            onNavigateTab = { viewModel.navigateTo(it) }
                        )
                    }

                    AppNavTab.SERVICES -> {
                        ServicesScreen(
                            services = filteredServices,
                            selectedPlatform = selectedPlatform,
                            searchQuery = searchQuery,
                            onSearchChange = { viewModel.setSearchQuery(it) },
                            onSelectPlatform = { viewModel.setPlatformFilter(it) },
                            onConfigureService = { viewModel.openCampaignBuilder(it) }
                        )
                    }

                    AppNavTab.ORDERS -> {
                        OrdersScreen(
                            orders = filteredOrders,
                            statusFilter = orderStatusFilter,
                            onStatusFilterChange = { viewModel.setOrderStatusFilter(it) },
                            onViewReport = { viewModel.openOrderReport(it) },
                            onStartNewCampaign = { viewModel.openCampaignBuilder() }
                        )
                    }

                    AppNavTab.CREATORS -> {
                        CreatorsScreen(
                            creators = filteredCreators,
                            selectedPlatform = selectedPlatform,
                            selectedCategory = creatorCategoryFilter,
                            onSelectPlatform = { viewModel.setPlatformFilter(it) },
                            onSelectCategory = { viewModel.setCreatorCategoryFilter(it) },
                            onBookCreator = { creator -> viewModel.openCampaignBuilder(creator = creator) },
                            onRegisterCreator = { viewModel.openCreatorRegister() }
                        )
                    }

                    AppNavTab.ANALYTICS -> {
                        AnalyticsScreen(
                            orders = allOrders
                        )
                    }

                    AppNavTab.PROFILE -> {
                        ProfileScreen(
                            userSession = userSession,
                            transactions = allTransactions,
                            onRoleChange = { viewModel.switchUserRole(it) },
                            onOpenTopUp = { viewModel.toggleTopUp(true) },
                            onDeleteAccount = { viewModel.deleteAccount() }
                        )
                    }
                }
            }
        }

        // Modals & Sheets
        if (isCampaignBuilderOpen) {
            CampaignBuilderSheet(
                formState = campaignForm,
                availableServices = servicesCatalog,
                walletBalance = userSession.walletBalanceUsd,
                onUpdateForm = { viewModel.updateCampaignForm(it) },
                onSubmitOrder = { viewModel.submitCampaignOrder() },
                onDismiss = { viewModel.closeCampaignBuilder() }
            )
        }

        selectedOrderForReport?.let { order ->
            CampaignReportSheet(
                order = order,
                onDismiss = { viewModel.closeOrderReport() }
            )
        }

        if (isCreatorRegisterOpen) {
            CreatorRegisterSheet(
                onRegister = { name, handle, plat, cat, price, reach, days, content, bio ->
                    viewModel.registerCreator(name, handle, plat, cat, price, reach, days, content, bio)
                },
                onDismiss = { viewModel.closeCreatorRegister() }
            )
        }

        if (isNotificationsOpen) {
            NotificationsSheet(
                notifications = allNotifications,
                onMarkAllRead = { viewModel.markAllNotificationsRead() },
                onDismiss = { viewModel.toggleNotifications(false) }
            )
        }

        if (isTopUpOpen) {
            TopUpWalletDialog(
                currentBalance = userSession.walletBalanceUsd,
                onConfirmTopUp = { viewModel.topUpWallet(it) },
                onDismiss = { viewModel.toggleTopUp(false) }
            )
        }

        fraudAlertMessage?.let { msg ->
            FraudWarningDialog(
                message = msg,
                onDismiss = { viewModel.dismissFraudAlert() }
            )
        }
    }
}
