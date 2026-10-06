package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CampaignOrder
import com.example.data.model.CampaignStatus
import com.example.data.model.MarketingService
import com.example.ui.components.PlatformBadge
import com.example.ui.components.StatusBadge
import com.example.ui.theme.BrandAmberWarning
import com.example.ui.theme.BrandEmeraldTrust
import com.example.ui.theme.BrandRoseDanger

@Composable
fun AdminScreen(
    orders: List<CampaignOrder>,
    services: List<MarketingService>,
    onApproveOrder: (Long, String) -> Unit,
    onStartOrder: (Long) -> Unit,
    onCompleteOrder: (Long) -> Unit,
    onRejectOrder: (Long, String) -> Unit,
    onUpdatePrice: (String, Double) -> Unit,
    onToggleService: (String) -> Unit,
    onSendBroadcast: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var rejectDialogOrder by remember { mutableStateOf<CampaignOrder?>(null) }
    var rejectReason by remember { mutableStateOf("Target page violates social media platform terms of service") }

    var editPriceService by remember { mutableStateOf<MarketingService?>(null) }
    var newPriceText by remember { mutableStateOf("") }

    var showBroadcastDialog by remember { mutableStateOf(false) }
    var broadcastTitle by remember { mutableStateOf("Platform Policy Update") }
    var broadcastMsg by remember { mutableStateOf("All marketing orders strictly maintain real verified human delivery.") }

    val pendingOrders = orders.filter { it.status == CampaignStatus.PENDING_REVIEW }
    val runningOrders = orders.filter { it.status == CampaignStatus.RUNNING || it.status == CampaignStatus.APPROVED }
    val totalRevenue = orders.filter { it.status != CampaignStatus.REJECTED }.sumOf { it.budgetUsd }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("admin_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = BrandAmberWarning.copy(alpha = 0.2f),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = BrandAmberWarning)
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Admin & Compliance Center",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Ad Operations, Pricing & Fraud Management",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Button(
                    onClick = { showBroadcastDialog = true },
                    modifier = Modifier.height(34.dp).testTag("admin_broadcast_button")
                ) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = null, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Broadcast", fontSize = 11.sp)
                }
            }
        }

        // Summary Revenue & Metrics
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AdminStatCard(
                    title = "Gross Ad Revenue",
                    value = "$${totalRevenue.toInt()} USD",
                    color = BrandEmeraldTrust,
                    modifier = Modifier.weight(1f)
                )
                AdminStatCard(
                    title = "Pending Review",
                    value = "${pendingOrders.size}",
                    color = BrandAmberWarning,
                    modifier = Modifier.weight(1f)
                )
                AdminStatCard(
                    title = "Active Delivery",
                    value = "${runningOrders.size}",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Approval Queue Section
        item {
            Text(
                text = "Campaign Approval Queue (${pendingOrders.size} Pending)",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (pendingOrders.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = "All campaigns reviewed! No orders awaiting approval.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(pendingOrders) { order ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth().testTag("pending_order_${order.id}")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            PlatformBadge(platform = order.platform, small = true)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = order.orderNumber,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "$${order.budgetUsd.toInt()} USD",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = order.campaignTitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = order.serviceTitle,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "URL: ${order.targetUrl}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                        Text(
                            text = "Audience: ${order.targetAudience} (${order.targetCountry})",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { onApproveOrder(order.id, "Approved for official placement") },
                                colors = ButtonDefaults.buttonColors(containerColor = BrandEmeraldTrust),
                                modifier = Modifier.weight(1f).height(36.dp).testTag("admin_approve_button_${order.id}")
                            ) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Approve", fontSize = 12.sp)
                            }

                            Button(
                                onClick = {
                                    rejectDialogOrder = order
                                    rejectReason = "Violates platform community standards or contains unverified links"
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = BrandRoseDanger),
                                modifier = Modifier.weight(1f).height(36.dp).testTag("admin_reject_button_${order.id}")
                            ) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Reject & Refund", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        // Active Delivery Controls (Start / Complete)
        item {
            Text(
                text = "Live Delivery Controls (${runningOrders.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        items(runningOrders) { order ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = order.orderNumber, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(6.dp))
                            StatusBadge(status = order.status)
                        }
                        Text(text = order.campaignTitle, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                        Text(text = "Reach: %,d".format(order.verifiedReach), fontSize = 11.sp, color = BrandEmeraldTrust)
                    }

                    if (order.status == CampaignStatus.APPROVED) {
                        Button(
                            onClick = { onStartOrder(order.id) },
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("Launch Live", fontSize = 11.sp)
                        }
                    } else if (order.status == CampaignStatus.RUNNING) {
                        Button(
                            onClick = { onCompleteOrder(order.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = BrandEmeraldTrust),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("Complete", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Service Catalog & Price Management
        item {
            Text(
                text = "Service Catalog & Pricing Controls (12 Services)",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        items(services) { service ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "#${service.serviceNumber} ${service.title}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Base: $${service.basePriceUsd.toInt()} USD • ${service.priceUnit}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = {
                            editPriceService = service
                            newPriceText = service.basePriceUsd.toInt().toString()
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Price", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                    }

                    IconButton(onClick = { onToggleService(service.id) }) {
                        Icon(
                            imageVector = if (service.isActive) Icons.Default.ToggleOn else Icons.Default.ToggleOff,
                            contentDescription = "Toggle",
                            tint = if (service.isActive) BrandEmeraldTrust else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }

    // Rejection Reason Dialog
    if (rejectDialogOrder != null) {
        val target = rejectDialogOrder!!
        AlertDialog(
            onDismissRequest = { rejectDialogOrder = null },
            title = { Text("Reject Order & Refund Client", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        text = "Order: ${target.orderNumber} ($${target.budgetUsd.toInt()} USD)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Specify compliance / policy reason:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = rejectReason,
                        onValueChange = { rejectReason = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Note: A full refund of $${target.budgetUsd.toInt()} will be instantly credited to the user's wallet.",
                        fontSize = 11.sp,
                        color = BrandRoseDanger
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onRejectOrder(target.id, rejectReason)
                        rejectDialogOrder = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandRoseDanger)
                ) {
                    Text("Confirm Rejection & Refund")
                }
            },
            dismissButton = {
                TextButton(onClick = { rejectDialogOrder = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Edit Price Dialog
    if (editPriceService != null) {
        val svc = editPriceService!!
        AlertDialog(
            onDismissRequest = { editPriceService = null },
            title = { Text("Adjust Service Price", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(text = svc.title, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = newPriceText,
                        onValueChange = { newPriceText = it },
                        label = { Text("Base Price (USD)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        newPriceText.toDoubleOrNull()?.let { p ->
                            onUpdatePrice(svc.id, p)
                        }
                        editPriceService = null
                    }
                ) {
                    Text("Save Price")
                }
            },
            dismissButton = {
                TextButton(onClick = { editPriceService = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Broadcast Notification Dialog
    if (showBroadcastDialog) {
        AlertDialog(
            onDismissRequest = { showBroadcastDialog = false },
            title = { Text("Send System Broadcast", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = broadcastTitle,
                        onValueChange = { broadcastTitle = it },
                        label = { Text("Notification Title") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = broadcastMsg,
                        onValueChange = { broadcastMsg = it },
                        label = { Text("Message Body") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onSendBroadcast(broadcastTitle, broadcastMsg)
                        showBroadcastDialog = false
                    }
                ) {
                    Text("Send Broadcast")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBroadcastDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun AdminStatCard(
    title: String,
    value: String,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                color = color
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
