package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.data.model.MarketingService
import com.example.data.model.SocialPlatform
import com.example.ui.theme.BrandEmeraldTrust
import com.example.ui.theme.BrandRoseDanger
import com.example.ui.viewmodel.CampaignFormState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampaignBuilderSheet(
    formState: CampaignFormState,
    availableServices: List<MarketingService>,
    walletBalance: Double,
    onUpdateForm: ((CampaignFormState) -> CampaignFormState) -> Unit,
    onSubmitOrder: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scrollState = rememberScrollState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier.testTag("campaign_builder_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState)
                .padding(bottom = 32.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Campaign,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Configure Legal Campaign",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Real Reach & Verified Human Placement",
                        fontSize = 12.sp,
                        color = BrandEmeraldTrust,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Platform selection
            Text(
                text = "Target Social Platform",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val platforms = listOf(
                    SocialPlatform.INSTAGRAM,
                    SocialPlatform.YOUTUBE,
                    SocialPlatform.TIKTOK,
                    SocialPlatform.FACEBOOK,
                    SocialPlatform.TELEGRAM
                )
                platforms.forEach { plat ->
                    val isSelected = formState.selectedPlatform == plat
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) plat.primaryColor.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onUpdateForm { it.copy(selectedPlatform = plat) } }
                            .testTag("campaign_plat_${plat.name.lowercase()}")
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = plat.displayName.take(5),
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) plat.primaryColor else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Service Info Card
            val activeService = formState.selectedService ?: availableServices.firstOrNull()
            if (activeService != null) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = activeService.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "Base $${activeService.basePriceUsd.toInt()}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = activeService.description,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = BrandEmeraldTrust,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = activeService.legalCompliancePledge,
                                fontSize = 10.sp,
                                color = BrandEmeraldTrust,
                                lineHeight = 13.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Campaign Title
            OutlinedTextField(
                value = formState.campaignTitle,
                onValueChange = { title -> onUpdateForm { it.copy(campaignTitle = title) } },
                label = { Text("Campaign Name / Title *") },
                placeholder = { Text("e.g. Summer App Launch Campaign") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("campaign_title_input")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Target URL
            OutlinedTextField(
                value = formState.targetUrl,
                onValueChange = { url -> onUpdateForm { it.copy(targetUrl = url) } },
                label = { Text("Target URL / Channel / Post Link *") },
                placeholder = { Text("https://instagram.com/p/... or youtube.com/...") },
                singleLine = true,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Link, contentDescription = null, modifier = Modifier.size(18.dp))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("campaign_url_input")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Target Audience
            OutlinedTextField(
                value = formState.targetAudience,
                onValueChange = { aud -> onUpdateForm { it.copy(targetAudience = aud) } },
                label = { Text("Target Audience Niche & Demographics") },
                placeholder = { Text("e.g. Tech Enthusiasts, 20-35 years old") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Target Country
            OutlinedTextField(
                value = formState.targetCountry,
                onValueChange = { country -> onUpdateForm { it.copy(targetCountry = country) } },
                label = { Text("Target Countries / Regions") },
                singleLine = true,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Public, contentDescription = null, modifier = Modifier.size(18.dp))
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Budget Slider & Reach Estimator
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Campaign Budget",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "$${formState.budgetUsd.toInt()} USD",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Slider(
                value = formState.budgetUsd.toFloat(),
                onValueChange = { v -> onUpdateForm { it.copy(budgetUsd = v.toDouble()) } },
                valueRange = 25f..500f,
                steps = 18,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("budget_slider")
            )

            // Dynamic Reach Calculation Box
            val estReach = (formState.budgetUsd * 140).toInt()
            val estImpressions = (estReach * 1.35).toInt()
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Estimated Verified Reach: ~${estReach / 1000}k to ${(estReach * 1.2).toInt() / 1000}k People",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "~${estImpressions / 1000}k Verified Impressions via official ad network algorithms",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Payment Gateway Selection
            Text(
                text = "Secure Payment Method",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))

            val paymentOptions = listOf(
                "Google Pay (Tokenized)",
                "Credit/Debit Card (SSL Gateway)",
                "Wallet Balance ($${walletBalance.toInt()} Available)"
            )

            paymentOptions.forEach { method ->
                val isSelected = formState.paymentMethod.startsWith(method.take(10))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onUpdateForm { it.copy(paymentMethod = method) } }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = { onUpdateForm { it.copy(paymentMethod = method) } }
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = when {
                            method.contains("Google") -> Icons.Default.Payment
                            method.contains("Wallet") -> Icons.Default.AccountBalanceWallet
                            else -> Icons.Default.CreditCard
                        },
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = method,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }

            // Zero Credential Statement
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = BrandEmeraldTrust,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Zero sensitive data stored. No passwords, card numbers, or social credentials are ever requested or stored on device.",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 14.sp
                    )
                }
            }

            if (formState.errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = formState.errorMessage,
                    color = BrandRoseDanger,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Submit Button
            Button(
                onClick = onSubmitOrder,
                enabled = !formState.isSubmitting,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("submit_campaign_order_button")
            ) {
                if (formState.isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Confirm & Launch Campaign ($${formState.budgetUsd.toInt()})",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
