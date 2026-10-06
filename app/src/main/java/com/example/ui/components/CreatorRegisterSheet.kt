package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.data.model.SocialPlatform
import com.example.ui.theme.BrandEmeraldTrust
import com.example.ui.theme.BrandPurpleCreator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatorRegisterSheet(
    onRegister: (
        name: String,
        handle: String,
        platform: SocialPlatform,
        category: String,
        startingPrice: Double,
        reach: String,
        deliveryDays: String,
        contentType: String,
        bio: String
    ) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scrollState = rememberScrollState()

    var name by remember { mutableStateOf("") }
    var handle by remember { mutableStateOf("") }
    var selectedPlatform by remember { mutableStateOf(SocialPlatform.INSTAGRAM) }
    var category by remember { mutableStateOf("Tech & Apps") }
    var priceStr by remember { mutableStateOf("75") }
    var reachStr by remember { mutableStateOf("50K+ Monthly Reach") }
    var deliveryDays by remember { mutableStateOf("3 Days") }
    var contentType by remember { mutableStateOf("Authentic UGC Video / Reel") }
    var bio by remember { mutableStateOf("") }
    var agreedZeroBots by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier.testTag("creator_register_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState)
                .padding(bottom = 32.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = null,
                    tint = BrandPurpleCreator,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Join Creator Marketplace",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Monetize genuine reach with real brands",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Creator / Channel Name *") },
                placeholder = { Text("e.g. Sarah Jenkins") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("creator_name_input")
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = handle,
                onValueChange = { handle = it },
                label = { Text("Public Handle / Link *") },
                placeholder = { Text("@sarah.tech or youtube.com/@channel") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("creator_handle_input")
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("Primary Platform", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf(SocialPlatform.INSTAGRAM, SocialPlatform.TIKTOK, SocialPlatform.YOUTUBE, SocialPlatform.TELEGRAM).forEach { p ->
                    val isSelected = selectedPlatform == p
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) p.primaryColor.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedPlatform = p }
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = p.displayName.take(5),
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) p.primaryColor else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Niche / Category") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = priceStr,
                    onValueChange = { priceStr = it },
                    label = { Text("Price (USD)") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = reachStr,
                    onValueChange = { reachStr = it },
                    label = { Text("Est. Reach") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = deliveryDays,
                    onValueChange = { deliveryDays = it },
                    label = { Text("Delivery Days") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = contentType,
                onValueChange = { contentType = it },
                label = { Text("Offered Content Type") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Short Bio & Audience Demographics") },
                placeholder = { Text("Describe your genuine audience, top countries, etc.") },
                maxLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Zero bot verification pledge checkbox
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { agreedZeroBots = !agreedZeroBots }
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = agreedZeroBots,
                        onCheckedChange = { agreedZeroBots = it }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "I solemnly certify that 100% of my followers and views are genuine human audiences. I agree to Free S Time's zero-bot audit policy and acknowledge selling fake engagement is strictly prohibited.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 15.sp
                    )
                }
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = {
                    if (name.isBlank() || handle.isBlank()) {
                        errorMessage = "Please enter your name and handle."
                        return@Button
                    }
                    if (!agreedZeroBots) {
                        errorMessage = "You must agree to the Zero-Bot policy."
                        return@Button
                    }
                    val price = priceStr.toDoubleOrNull() ?: 50.0
                    onRegister(
                        name,
                        handle,
                        selectedPlatform,
                        category,
                        price,
                        reachStr,
                        deliveryDays,
                        contentType,
                        if (bio.isBlank()) "Verified creator on ${selectedPlatform.displayName}" else bio
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("submit_creator_button")
            ) {
                Text("Submit Creator Profile for Verification")
            }
        }
    }
}
